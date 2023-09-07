package com.example.demo.e2e

import com.example.demo.AbstractIntegrationTest
import com.example.demo.adapter.`in`.web.dto.AddAccountResource
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.matchers.ints.shouldBeGreaterThan
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate

class AccountE2ETests : AbstractIntegrationTest() {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun second() {
        // given
        val requestBody = AddAccountResource("carrot", 30)

        // when
        val responseEntity =
            testRestTemplate.postForEntity("/account", requestBody, Long::class.java)

        // then
        responseEntity.body!!.toInt() shouldBeGreaterThan 0
    }

}
