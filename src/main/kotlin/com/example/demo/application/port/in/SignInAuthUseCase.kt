package com.example.demo.application.port.`in`

import com.example.demo.adapter.`in`.web.dto.InquiryTokenResource

interface SignInAuthUseCase {
    fun signIn(signInAuthQuery: SignInAuthQuery): InquiryTokenResource
}