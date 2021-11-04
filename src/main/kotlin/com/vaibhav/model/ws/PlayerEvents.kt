package com.vaibhav.model.ws

import com.vaibhav.util.Constants.TYPE_PLAYER_EVENT
import kotlinx.serialization.Serializable

@Serializable
data class PlayerEvents(
    val playerClientId: String,
    val eventType: Int
): BaseModel(TYPE_PLAYER_EVENT) {

    companion object {
        const val TYPE_PLAYER_JOINED = 0
        const val TYPE_PLAYER_LEFT = 1
    }
}
