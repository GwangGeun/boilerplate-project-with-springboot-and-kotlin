package com.example.demo.adapter.`in`.web

import com.example.demo.adapter.`in`.web.dto.AddAccountResource
import com.example.demo.adapter.`in`.web.dto.InquiryAccountNameResource
import com.example.demo.adapter.`in`.web.dto.UpdateAccountNameResource
import com.example.demo.application.port.`in`.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(
    val updateUserCase: UpdateAccountUseCase,
    val getAccountUserCase: GetAccountUserCase,
    val addAccountUseCase: AddAccountUseCase
) {

    @GetMapping("/account/username/{id}")
    fun getAccountUserName(@PathVariable id: Long): InquiryAccountNameResource {
        return getAccountUserCase.getAccountUserName(id)
    }

    @PutMapping("/account/username/{id}")
    fun updateAccountName(
        @PathVariable id: Long,
        @RequestBody updateAccountNameResource: UpdateAccountNameResource
    ) {
        updateUserCase.updateAccountName(
            id,
            UpdateAccountNameCommand.from(updateAccountNameResource)
        )
    }

    @PostMapping("/account")
    fun addAccount(
        @RequestBody addAccountResource: AddAccountResource
    ): ResponseEntity<Long> {
        val id = addAccountUseCase.addAccountName(AddAccountCommand.from(addAccountResource))
        return ResponseEntity<Long>(id, HttpStatus.CREATED)
    }

}