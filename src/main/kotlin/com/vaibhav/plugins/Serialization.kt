package com.vaibhav.plugins

import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.features.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        Gson()
    }
}
