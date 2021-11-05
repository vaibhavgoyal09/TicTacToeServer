package com.vaibhav

import com.google.gson.Gson
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.vaibhav.plugins.*

val gson = Gson()
val socketConnection = SocketConnection()

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
