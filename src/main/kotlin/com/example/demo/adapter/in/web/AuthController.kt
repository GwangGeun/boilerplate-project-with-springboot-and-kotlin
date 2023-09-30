package com.example.demo.adapter.`in`.web

import com.example.demo.adapter.`in`.web.dto.InquiryTokenResource
import com.example.demo.adapter.`in`.web.dto.SignInAuthResource
import com.example.demo.application.port.`in`.SignInAuthUseCase
import com.example.demo.application.port.`in`.SignInAuthQuery
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    val signInAuthCase: SignInAuthUseCase
) {

    @PostMapping("/signin")
    fun signIn(@RequestBody signInAuthResource: SignInAuthResource): ResponseEntity<InquiryTokenResource> {
        return try {
            val jwtToken = signInAuthCase.signIn(SignInAuthQuery.from(signInAuthResource))
            ResponseEntity(InquiryTokenResource(jwtToken), HttpStatus.OK)
        } catch (e: Exception){
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

}