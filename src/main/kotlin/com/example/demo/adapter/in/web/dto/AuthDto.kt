package com.example.demo.adapter.`in`.web.dto

data class SignInAuthResource(val name: String, val password: String)

data class InquiryTokenResource(val jwtToken: String, val refreshToken: String = "")