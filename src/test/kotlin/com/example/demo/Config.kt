package com.example.demo

import io.kotest.core.config.AbstractProjectConfig

// TODO - https://kotest.io/docs/extensions/spring.html
// DONE - https://kotest.io/docs/framework/project-config.html#parallelism
object KotestProjectConfig : AbstractProjectConfig() {
    override val parallelism = Runtime.getRuntime().availableProcessors()
}