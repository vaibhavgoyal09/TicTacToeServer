package com.vaibhav.plugins

import com.vaibhav.routes.http.roomRoutes
import com.vaibhav.routes.websockets.gameSocketRoute
import com.vaibhav.service.RoomService
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.routing.*

fun Application.configureRouting() {

    install(Locations) {
    }

    val roomService = RoomService()

    routing {
        roomRoutes(roomService)
        gameSocketRoute()
    }
}
