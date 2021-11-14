package com.vaibhav.model.ws

import com.vaibhav.model.Room
import com.vaibhav.util.Constants.TYPE_PHASE_CHANGE

data class GamePhaseChange(
    var phase: Room.GamePhase?,
    var time: Long
): BaseModel(TYPE_PHASE_CHANGE)
