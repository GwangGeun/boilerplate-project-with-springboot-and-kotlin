package com.example.demo.adapter.out.db

import com.example.demo.adapter.out.db.account.AccountEntity
import com.example.demo.adapter.out.db.account.AccountRepository
import com.example.demo.application.port.`in`.AddAccountCommand
import com.example.demo.application.port.out.InsertAccount
import com.example.demo.application.port.out.LoadAccount
import com.example.demo.application.port.out.UpdateAccountState
import org.springframework.stereotype.Component

@Component
class AccountPersistenceAdapter(
    val accountRepository: AccountRepository
) : LoadAccount, UpdateAccountState, InsertAccount {
    override fun getAccountName(id: Long): String {
        val optional = accountRepository.findById(id)
        return if (optional.isPresent) optional.get().name
        else ""
    }

    override fun updateAccountName(id: Long, toBe: String) {
        val optional = accountRepository.findById(id)
        if (optional.isPresent) optional.get().name = toBe
    }

    override fun insertAccount(addAccountCommand: AddAccountCommand): Long {
        val toEntity = AccountEntity.toEntity(addAccountCommand)
        return accountRepository.save(toEntity).id
    }

}