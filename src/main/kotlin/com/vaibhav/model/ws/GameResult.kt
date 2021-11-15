package com.vaibhav.model.ws

import com.vaibhav.util.Constants.TYPE_GAME_RESULT

data class GameResult(
    val resultType: Int
): BaseModel(TYPE_GAME_RESULT) {
    companion object {
        const val TYPE_GAME_WON = 0
        const val TYPE_GAME_LOST = 1
        const val TYPE_GAME_DRAW = 2
    }
}
