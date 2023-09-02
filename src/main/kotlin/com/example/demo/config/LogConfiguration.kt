package com.example.demo.config

import ch.qos.logback.access.tomcat.LogbackValve
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class LogConfiguration {
    @Bean
    fun servletContainer(): TomcatServletWebServerFactory {
        val tomcat = TomcatServletWebServerFactory()
        val logbackValve = LogbackValve()
        logbackValve.filename = "logback-access.xml"
        logbackValve.isQuiet = true
        tomcat.addContextValves(logbackValve)
        return tomcat
    }
}