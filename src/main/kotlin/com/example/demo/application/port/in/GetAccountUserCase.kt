package com.example.demo.application.port.`in`

import com.example.demo.adapter.`in`.web.dto.InquiryAccountNameResource

interface GetAccountUserCase {
    fun getAccountUserName(id: Long) : InquiryAccountNameResource
}