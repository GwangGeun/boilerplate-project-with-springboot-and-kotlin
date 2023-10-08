package com.example.demo.application.service

import com.example.demo.adapter.`in`.web.dto.InquiryTokenResource
import com.example.demo.adapter.out.db.AuthPersistenceAdapter
import com.example.demo.application.port.`in`.*
import com.example.demo.common.JwtTokenProvider
import com.example.demo.common.Token
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException
import kotlin.reflect.jvm.internal.impl.util.CheckResult.IllegalSignature

@Service
class AuthService(
    val authPersistenceAdapter: AuthPersistenceAdapter,
    val passwordEncoder: PasswordEncoder,
    val jwtTokenProvider: JwtTokenProvider
) : SignInAuthUseCase, RefreshTokenUseCase, TokenUseCase {

    @Transactional
    override fun signIn(signInAuthQuery: SignInAuthQuery): InquiryTokenResource {
        val account = authPersistenceAdapter.getAccount(signInAuthQuery.name)
            ?: throw IllegalArgumentException("can not find account")
        if (!passwordEncoder.matches(signInAuthQuery.password, account.password)) {
            throw IllegalArgumentException("password is incorrect")
        }
        val jwtToken = jwtTokenProvider.createToken(account.id, Token.ACCESS)
        val refreshToken = jwtTokenProvider.createToken(account.id, Token.REFRESH)
        account.refreshToken = refreshToken
        return InquiryTokenResource(jwtToken, refreshToken)
    }

    override fun validateRefresh(refreshTokenQuery: RefreshTokenQuery) {
        if(!jwtTokenProvider.validateToken(refreshTokenQuery.refreshToken, Token.REFRESH)) throw IllegalStateException("token expired")
        authPersistenceAdapter.getAccountByRefreshToken(refreshTokenQuery.refreshToken)
            ?: throw IllegalArgumentException("can not find account")
    }

    override fun generateToken(id: Long): InquiryTokenResource {
        val account = authPersistenceAdapter.getAccountById(id) ?: throw IllegalArgumentException("can not find account")
        val accessToken = jwtTokenProvider.createToken(id, Token.ACCESS)
        val newRefreshToken = jwtTokenProvider.createToken(id, Token.REFRESH)
        account.refreshToken = newRefreshToken
        return InquiryTokenResource(accessToken, newRefreshToken)
    }

}