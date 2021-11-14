package com.vaibhav.model

import io.ktor.http.cio.websocket.*

data class Player(
    val userName: String,
    var socket: WebSocketSession,
    val clientId: String,
    var score: Int = 0,
    var symbol: Int = NO_SYMBOL
) {

    var isOnline = true

    companion object {
        const val NO_SYMBOL = 0
        const val SYMBOL_X = 1
        const val SYMBOL_O = 2
    }
}
