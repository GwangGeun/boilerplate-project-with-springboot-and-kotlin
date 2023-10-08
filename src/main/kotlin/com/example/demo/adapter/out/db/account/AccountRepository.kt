package com.example.demo.adapter.out.db.account

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<AccountEntity, Long> {

    fun findByName(name: String): AccountEntity?

    fun findByRefreshToken(name: String): AccountEntity?

}