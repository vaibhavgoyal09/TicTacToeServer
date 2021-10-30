package com.vaibhav.plugins

import io.ktor.application.*
import io.ktor.locations.*

fun Application.configureRouting() {
    install(Locations) {
    }
}
