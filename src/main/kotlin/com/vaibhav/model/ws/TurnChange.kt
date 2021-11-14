package com.vaibhav.model.ws

import com.vaibhav.util.Constants.TYPE_TURN_CHANGE

data class TurnChange(
    val playerWithTurnClientId: String,
    val previousMove: Int
): BaseModel(TYPE_TURN_CHANGE)
