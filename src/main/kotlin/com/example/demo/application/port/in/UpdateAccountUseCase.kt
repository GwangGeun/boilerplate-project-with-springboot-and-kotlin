package com.example.demo.application.port.`in`

interface UpdateAccountUseCase {
    fun updateAccountName(id: Long, updateAccountNameCommand: UpdateAccountNameCommand)

}