package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Demo2ApplicationTests : AbstractIntegrationTest(){

	@Test
	fun contextLoads() {
		println("hello world2")
	}

}
