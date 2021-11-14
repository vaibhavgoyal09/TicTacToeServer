package com.vaibhav.model.ws

import com.vaibhav.util.Constants.TYPE_GAME_MOVE

data class GameMove(
    val position: Int
): BaseModel(TYPE_GAME_MOVE)
