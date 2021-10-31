package com.vaibhav.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RoomResponse(
    val name: String,
    val playersCount: Int
)
