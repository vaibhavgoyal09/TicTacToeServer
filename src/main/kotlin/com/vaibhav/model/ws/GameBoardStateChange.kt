package com.vaibhav.model.ws

import com.vaibhav.util.Constants.TYPE_GAME_BOARD_STATE_CHANGED

data class GameBoardStateChange(
    val state: List<Int> = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
): BaseModel(TYPE_GAME_BOARD_STATE_CHANGED)
