package com.example.demo.application.port.`in`

import com.example.demo.adapter.`in`.web.dto.AddAccountResource

class AddAccountCommand private constructor (val name: String, var password: String, val age: Int) {
    companion object {
        fun from(addAccountResource: AddAccountResource) =
            AddAccountCommand(addAccountResource.name, addAccountResource.password, addAccountResource.age)
    }
}
