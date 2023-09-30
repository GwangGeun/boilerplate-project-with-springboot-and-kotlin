package com.example.demo.application.port.`in`

interface SignInAuthUseCase {
    fun signIn(signInAuthQuery: SignInAuthQuery): String
}