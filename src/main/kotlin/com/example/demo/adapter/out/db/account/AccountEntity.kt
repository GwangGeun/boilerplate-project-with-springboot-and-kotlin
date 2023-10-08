package com.example.demo.adapter.out.db.account

import com.example.demo.adapter.out.db.common.BaseEntity
import com.example.demo.application.port.`in`.AddAccountCommand
import com.fasterxml.jackson.databind.ser.Serializers.Base
import jakarta.persistence.*

@Entity(name = "account")
class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    val id: Long = 0L,
    var name: String,
    var password: String,
    var age: Int,
    var refreshToken: String? = ""
) : BaseEntity() {
    companion object {
        fun toEntity(addAccountCommand: AddAccountCommand): AccountEntity {
            /**
             * require(Boolean)
             * - throws IllegalArgumentException when its argument is false. Use it to test function arguments.
             * check(Boolean)
             * - throws IllegalStateException when its argument is false. Use it to test object state.
             * assert(Boolean)
             * - throws AssertionError when its argument is false (but only if JVM assertions are enabled with -ea). Use it to clarify outcomes and check your work.
             */
            check(addAccountCommand.age > 0) { "age must be greater than 0" }
            return AccountEntity(
                name = addAccountCommand.name,
                password = addAccountCommand.password,
                age = addAccountCommand.age
            )
        }
    }

    fun updateRefreshToken(refreshToken: String?){
        this.refreshToken = refreshToken
    }

}