package com.example.demo.config

import com.example.demo.common.LoggerExtensions.createLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*
import kotlin.random.Random

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
class JpaConfig {

    val log = createLogger()

    @Bean
    fun auditorProvider(): AuditorAware<String>? {
        return AuditorAware {
            Optional.of(Random.nextInt(10000).toString())
        }
    }

}