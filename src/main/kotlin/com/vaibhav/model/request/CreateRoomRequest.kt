package com.vaibhav.model.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val roomName: String
)
