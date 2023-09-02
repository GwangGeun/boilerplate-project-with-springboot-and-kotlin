package com.example.demo.adapter.`in`.web

import com.example.demo.adapter.`in`.web.dto.InquiryAccountNameResource
import com.example.demo.application.port.`in`.AddAccountUseCase
import com.example.demo.application.port.`in`.GetAccountUserCase
import com.example.demo.application.port.`in`.UpdateAccountUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

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
    private lateinit var addAccountUseCase: AddAccountUseCase

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun updateUserCase() = mockk<UpdateAccountUseCase>()

        @Bean
        fun getAccountUserCase() = mockk<GetAccountUserCase>()

        @Bean
        fun addAccountUseCase() = mockk<AddAccountUseCase>()
    }

    @Test
    fun test() {
        every { getAccountUserCase.getAccountUserName(1) } returns InquiryAccountNameResource("a")

        mockMvc.perform(MockMvcRequestBuilders.get("/account/username/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("a"))
            .andReturn()

        verify { getAccountUserCase.getAccountUserName(1) }
    }

}