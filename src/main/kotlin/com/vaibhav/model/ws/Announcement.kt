package com.vaibhav.model.ws

import com.vaibhav.util.Constants.TYPE_ANNOUNCEMENT

data class Announcement(
    val announcementType: Int,
    val playerClientId: String
): BaseModel(TYPE_ANNOUNCEMENT) {

    companion object {
        const val TYPE_PLAYER_JOINED = 0
        const val TYPE_PLAYER_LEFT = 1
        const val TYPE_PLAYER_WON = 2
        const val TYPE_MATCH_DRAW = 3
    }
}
