package com.example.demo.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LoggerExtensions {
    @Suppress("UnusedReceiverParameter")
    inline fun <reified T : Any> T.createLogger(): Logger = LoggerFactory.getLogger(T::class.java)
}