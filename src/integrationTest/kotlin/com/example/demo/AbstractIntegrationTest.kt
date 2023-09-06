package com.example.demo

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
abstract class AbstractIntegrationTest{

    companion object {
        val mysqlContainer = MySQLContainer("mysql:8.1.0")

//        val mysqlContainer = GenericContainer<Nothing>("mysql:8.1.0")
//            .apply { withExposedPorts(3306) }

    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            mysqlContainer.start()

            println(mysqlContainer.firstMappedPort)

            TestPropertyValues.of(
                "spring.redis.host=${mysqlContainer.host}",
                "spring.redis.port=${mysqlContainer.firstMappedPort}"
            ).applyTo(configurableApplicationContext.environment)
        }
    }

}