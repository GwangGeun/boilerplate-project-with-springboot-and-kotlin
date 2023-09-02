package com.example.demo.application.port.out

import com.example.demo.application.port.`in`.AddAccountCommand

interface InsertAccount {
    fun insertAccount(addAccountCommand: AddAccountCommand): Long
}