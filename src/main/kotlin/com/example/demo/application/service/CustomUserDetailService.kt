package com.example.demo.application.service

import com.example.demo.adapter.out.db.account.AccountRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    val accountRepository: AccountRepository
) : UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails {
        val accountId = id.toLong()
        val accountEntity = accountRepository.findById(accountId)
            .orElseThrow { UsernameNotFoundException("Account $accountId not found") }
        return User(accountEntity.name, "", grantedAuthorities())
    }

    private fun grantedAuthorities(): List<GrantedAuthority> {
        // Can add ROLE, Authority if needed
        return AuthorityUtils.createAuthorityList("ROLE_MANAGER")
    }
}