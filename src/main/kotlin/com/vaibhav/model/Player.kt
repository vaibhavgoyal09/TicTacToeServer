package com.vaibhav.model

import io.ktor.http.cio.websocket.*
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val userName: String,
    var socket: WebSocketSession,
    val clientId: String,
    var score: Int = 0
) {

    var isOnline = true
}
