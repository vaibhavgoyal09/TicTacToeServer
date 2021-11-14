package com.vaibhav.model.response

data class JoinRoomResponse(
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val existingPlayerUserName: String? = null
)
