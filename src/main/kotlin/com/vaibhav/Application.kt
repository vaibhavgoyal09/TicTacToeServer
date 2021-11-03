package com.vaibhav

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.vaibhav.plugins.*

fun main() {

    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080

    embeddedServer(Netty, port = port, host = "0.0.0.0") {
        configureDI()
        configureSockets()
        configureRouting()
        configureSerialization()
        configureSession()
    }.start(wait = true)
}
