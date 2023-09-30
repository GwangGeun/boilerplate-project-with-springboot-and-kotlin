package com.example.demo.application.port.out

import com.example.demo.adapter.out.db.account.AccountEntity

interface LoadAccountEntity {
    fun getAccount(name: String): AccountEntity?
}