package com.example.demo.application.service

import com.example.demo.adapter.out.db.AuthPersistenceAdapter
import com.example.demo.application.port.`in`.SignInAuthQuery
import com.example.demo.application.port.`in`.SignInAuthUseCase
import com.example.demo.common.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.reflect.jvm.internal.impl.util.CheckResult.IllegalSignature

@Service
class AuthService(
    val authPersistenceAdapter: AuthPersistenceAdapter,
    val passwordEncoder: PasswordEncoder,
    val jwtTokenProvider: JwtTokenProvider
) : SignInAuthUseCase {
    override fun signIn(signInAuthQuery: SignInAuthQuery): String {
        val account = authPersistenceAdapter.getAccount(signInAuthQuery.name)
            ?: throw IllegalArgumentException("User can not find")
        if(!passwordEncoder.matches(signInAuthQuery.password, account.password)){
            throw IllegalArgumentException("password is incorrect")
        }
        return jwtTokenProvider.createToken(account.id)
    }

}