package com.vaibhav.model

import io.ktor.http.cio.websocket.*

data class Player(
    val userName: String,
    var socket: WebSocketSession,
    val clientId: String,
    var score: Int = 0
) {

    var isOnline = true
}
