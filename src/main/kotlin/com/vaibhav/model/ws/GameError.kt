package com.vaibhav.model.ws

import kotlinx.serialization.Serializable

@Serializable
data class GameError(
    val errorType: Int
) {
    companion object {
        const val TYPE_ROOM_NOT_FOUND = 0
    }
}
