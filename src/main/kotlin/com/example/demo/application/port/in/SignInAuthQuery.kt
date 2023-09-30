package com.example.demo.application.port.`in`

import com.example.demo.adapter.`in`.web.dto.SignInAuthResource

class SignInAuthQuery private constructor(val name: String, val password: String) {

    companion object {
        fun from(signInAuthResource: SignInAuthResource) =
            SignInAuthQuery(signInAuthResource.name, signInAuthResource.password)
    }
}