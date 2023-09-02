package com.example.demo.application.port.`in`

interface AddAccountUseCase {
    fun addAccountName(addAccountCommand: AddAccountCommand): Long

}