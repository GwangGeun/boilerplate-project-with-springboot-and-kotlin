package com.example.demo.application.port.out

import com.example.demo.adapter.out.db.account.AccountEntity

interface LoadRefreshToken {
    fun getAccountByRefreshToken(token: String): AccountEntity?
}