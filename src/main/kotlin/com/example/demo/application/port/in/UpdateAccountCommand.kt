package com.example.demo.application.port.`in`

import com.example.demo.adapter.`in`.web.dto.UpdateAccountNameResource

data class UpdateAccountNameCommand(val userName: String){
    companion object {
        fun from(updateAccountNameResource: UpdateAccountNameResource) = UpdateAccountNameCommand(updateAccountNameResource.name)
    }
}

data class UpdateAccountAgeCommand(val userAge: Int)