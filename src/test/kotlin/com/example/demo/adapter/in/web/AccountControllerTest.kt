package com.example.demo.adapter.`in`.web

import com.example.demo.adapter.`in`.web.dto.InquiryAccountNameResource
import com.example.demo.application.port.`in`.GetAccountUserCase
import com.example.demo.application.port.`in`.UpdateAccountUseCase
import com.example.demo.common.JwtTokenProvider
import com.example.demo.common.Token
import com.example.demo.config.JwtAuthenticationFilter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/**
 * 1. WebMvcTest Overview - https://spring.io/guides/gs/testing-web/
 * 2. Example - https://medium.com/@darych90/kotlin-spring-boot-mockk-6d1c1a6463ac
 * 3. Good library to consider - https://github.com/Ninja-Squad/springmockk
 * 4. Test Slice - https://docs.spring.io/spring-boot/docs/current/reference/html/test-auto-configuration.html#appendix.test-auto-configuration
 *
 * Note - There's no MockBean created by SpringBoot using Mockito in mockk
 */
@WebMvcTest(AccountController::class)
class AccountControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var updateUserCase: UpdateAccountUseCase

    @Autowired
    private lateinit var getAccountUserCase: GetAccountUserCase

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private val TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidHlwZSI6IkFDQ0VTUyIsImlhdCI6MTY5NjczNTY2NiwiZXhwIjoxNjk2NzM3NDY2fQ.2NaezDSkTvlQ41CUHVHQb2s9lnACbkPCqqAnfMPr598"

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun updateUserCase() = mockk<UpdateAccountUseCase>()

        @Bean
        fun getAccountUserCase() = mockk<GetAccountUserCase>()

        @Bean
        fun jwtTokenProvider() = mockk<JwtTokenProvider>()
    }

    @BeforeEach
    fun setUp(){
        jwtAuthenticationFilter = JwtAuthenticationFilter(jwtTokenProvider)
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter<DefaultMockMvcBuilder>(jwtAuthenticationFilter, "/account/username/1")
            .build()
    }

    @Test
    fun test() {
        val createAuthorityList = AuthorityUtils.createAuthorityList("ROLE_MANAGER")
        val user = User("name", "", createAuthorityList)
        every { getAccountUserCase.getAccountUserName(1) } returns InquiryAccountNameResource("a")
        every { jwtTokenProvider.resolveToken(any()) } returns TOKEN
        every { jwtTokenProvider.validateToken(TOKEN, Token.ACCESS) } returns true
        every { jwtTokenProvider.getAuthentication(TOKEN, Token.ACCESS) } returns UsernamePasswordAuthenticationToken(user, "", user.authorities)

        mockMvc.perform(MockMvcRequestBuilders.get("/account/username/1").header("Authorization", TOKEN))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("a"))
            .andReturn()

        verify { getAccountUserCase.getAccountUserName(1) }
    }

}