package com.example.demo.adapter.`in`.web

import com.example.demo.adapter.`in`.web.dto.*
import com.example.demo.application.port.`in`.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

@RestController
class AuthController(
    val signInAuthCase: SignInAuthUseCase,
    val addAccountUseCase: AddAccountUseCase,
    val refreshTokenUseCase: RefreshTokenUseCase,
    val tokenUseCase: TokenUseCase
) {

    @PostMapping("/signin")
    fun signIn(@RequestBody signInAuthResource: SignInAuthResource): ResponseEntity<InquiryTokenResource> {
        return try {
            val inquiryTokenResource = signInAuthCase.signIn(SignInAuthQuery.from(signInAuthResource))
            ResponseEntity(inquiryTokenResource, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/signup")
    fun addAccount(
        @RequestBody addAccountResource: AddAccountResource
    ): ResponseEntity<Long> {
        val id = addAccountUseCase.addAccountName(AddAccountCommand.from(addAccountResource))
        return ResponseEntity<Long>(id, HttpStatus.CREATED)
    }

    @PostMapping("/refresh")
    fun validateRefresh(
        @RequestBody validateRefreshTokenResource: ValidateRefreshTokenResource
    ): ResponseEntity<InquiryTokenResource> {
        return try {
            refreshTokenUseCase.validateRefresh(RefreshTokenQuery.from(validateRefreshTokenResource))
            val token = tokenUseCase.generateToken(validateRefreshTokenResource.accountId)
            return ResponseEntity(token, HttpStatus.CREATED)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } catch (e: IllegalStateException) {
            ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

}