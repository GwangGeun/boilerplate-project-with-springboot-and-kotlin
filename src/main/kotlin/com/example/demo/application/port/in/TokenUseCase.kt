package com.example.demo.application.port.`in`

import com.example.demo.adapter.`in`.web.dto.InquiryTokenResource

interface TokenUseCase {
    fun generateToken(id: Long): InquiryTokenResource
}