package com.example.demo.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

/**
 * Domain logic test area
 */
class AccountTest : StringSpec({
    "strings.length should return size of string" {
        "hello".length shouldBe 5
    }
})
