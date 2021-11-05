package com.vaibhav.model.ws

import com.vaibhav.util.Constants.TYPE_JOIN_ROOM

data class JoinRoom(
    val userName: String,
    val roomName: String,
    val clientId: String
): BaseModel(TYPE_JOIN_ROOM)
