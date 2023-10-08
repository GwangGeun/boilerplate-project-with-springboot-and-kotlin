package com.example.demo.adapter.out.db

import com.example.demo.adapter.out.db.account.AccountEntity
import com.example.demo.adapter.out.db.account.AccountRepository
import com.example.demo.application.port.out.LoadAccountEntity
import com.example.demo.application.port.out.LoadRefreshToken
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class AuthPersistenceAdapter(
    val accountRepository: AccountRepository
) : LoadAccountEntity, LoadRefreshToken {
    override fun getAccount(name: String): AccountEntity? {
        return accountRepository.findByName(name)
    }

    override fun getAccountById(id: Long): AccountEntity? {
        return accountRepository.findById(id).get()
    }

    override fun getAccountByRefreshToken(token: String): AccountEntity? {
        return accountRepository.findByRefreshToken(token)
    }

}