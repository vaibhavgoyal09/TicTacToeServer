package com.vaibhav.plugins

import com.vaibhav.routes.http.roomRoutes
import com.vaibhav.service.RoomService
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    install(Locations) {
    }

    val roomService by inject<RoomService>()

    routing {
        roomRoutes(roomService)
    }
}
