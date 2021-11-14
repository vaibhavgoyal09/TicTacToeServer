package com.vaibhav.model.ws

data class GameError(
    val message: String? = null,
    val errorType: Int
) {
    companion object {
        const val TYPE_ROOM_NOT_FOUND = 0
        const val TYPE_GAME_MOVE_ERROR = 1
    }
}
