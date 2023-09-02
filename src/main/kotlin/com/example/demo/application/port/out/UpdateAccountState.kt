package com.example.demo.application.port.out

interface UpdateAccountState {

    fun updateAccountName(id: Long, toBe: String)
}