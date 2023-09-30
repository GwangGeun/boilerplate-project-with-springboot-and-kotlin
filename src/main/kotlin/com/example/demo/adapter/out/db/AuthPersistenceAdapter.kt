package com.example.demo.adapter.out.db

import com.example.demo.adapter.out.db.account.AccountEntity
import com.example.demo.adapter.out.db.account.AccountRepository
import com.example.demo.application.port.out.LoadAccountEntity
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class AuthPersistenceAdapter(
    val accountRepository: AccountRepository
) : LoadAccountEntity {
    override fun getAccount(name: String): AccountEntity? {
        return accountRepository.findByName(name)
    }

}