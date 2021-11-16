package com.vaibhav.plugins

import com.vaibhav.session.TicTacToeGameSession
import io.ktor.application.*
import io.ktor.sessions.*
import io.ktor.util.*

fun Application.configureSession() {
    install(Sessions) {
        cookie<TicTacToeGameSession>("SESSION")
    }

    intercept(ApplicationCallPipeline.Features) {
        if (call.sessions.get<TicTacToeGameSession>() == null) {
            val clientId = call.parameters["clientId"]!!
            call.sessions.set(TicTacToeGameSession(clientId, generateNonce()))
        }
    }
}