package com.example.demo.application.service

import com.example.demo.adapter.`in`.web.dto.UpdateAccountNameResource
import com.example.demo.adapter.out.db.AccountPersistenceAdapter
import com.example.demo.application.port.`in`.UpdateAccountNameCommand
import io.kotest.core.spec.style.FunSpec
import io.mockk.*
import org.springframework.security.crypto.password.PasswordEncoder

class UpdateAccountServiceTest : FunSpec({

    lateinit var accountPersistenceAdapter: AccountPersistenceAdapter
    lateinit var updateAccountService: UpdateAccountService
    lateinit var passwordEncoder: PasswordEncoder

    /**
     * kotest lifecycle
     * - https://kotest.io/docs/framework/lifecycle-hooks.html
     * - https://isntyet.github.io/kotlin/Kotest-%ED%95%B4%EB%B3%B4%EA%B8%B0/
     */
    beforeSpec {
    }

    beforeTest {
        clearAllMocks()
        accountPersistenceAdapter = mockk<AccountPersistenceAdapter>()
        passwordEncoder = spyk<PasswordEncoder>()
        updateAccountService = UpdateAccountService(accountPersistenceAdapter, passwordEncoder)
    }


    test("account name updated successfully by sending its request to accountPersistenceAdapter") {

        val id = 1L
        val toBeName = "toBe"
        every { accountPersistenceAdapter.updateAccountName(id, toBeName) } just runs

        updateAccountService.updateAccountName(1L, UpdateAccountNameCommand.from(UpdateAccountNameResource(toBeName)))

        verify(exactly = 1) { accountPersistenceAdapter.updateAccountName(id, toBeName) }
    }


})