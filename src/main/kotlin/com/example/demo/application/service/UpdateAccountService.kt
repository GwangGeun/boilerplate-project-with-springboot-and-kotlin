package com.example.demo.application.service

import com.example.demo.adapter.`in`.web.dto.InquiryAccountNameResource
import com.example.demo.adapter.out.db.AccountPersistenceAdapter
import com.example.demo.application.port.`in`.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateAccountService(
    val accountPersistenceAdapter: AccountPersistenceAdapter,
    val passwordEncoder: PasswordEncoder
) : UpdateAccountUseCase, GetAccountUserCase, AddAccountUseCase {

    @Transactional
    override fun updateAccountName(id: Long, updateAccountNameCommand: UpdateAccountNameCommand) {
        accountPersistenceAdapter.updateAccountName(id, updateAccountNameCommand.userName)
    }

    override fun getAccountUserName(id: Long): InquiryAccountNameResource {
        val accountName = accountPersistenceAdapter.getAccountName(id)
        return InquiryAccountNameResource(accountName)
    }

    @Transactional
    override fun addAccountName(addAccountCommand: AddAccountCommand): Long {
        addAccountCommand.password = passwordEncoder.encode(addAccountCommand.password)
        return accountPersistenceAdapter.insertAccount(addAccountCommand)
    }

}