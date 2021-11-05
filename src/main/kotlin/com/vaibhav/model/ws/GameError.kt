package com.vaibhav.model.ws

data class GameError(
    val errorType: Int
) {
    companion object {
        const val TYPE_ROOM_NOT_FOUND = 0
    }
}
