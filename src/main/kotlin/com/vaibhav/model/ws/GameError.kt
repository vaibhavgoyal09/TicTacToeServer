package com.vaibhav.model.ws

data class GameError(
    val errorType: Int,
    val message: String? = null,
) {
    companion object {
        const val TYPE_ROOM_NOT_FOUND = 0
        const val TYPE_INVALID_MOVE = 1
    }
}
