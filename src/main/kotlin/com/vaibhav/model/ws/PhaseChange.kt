package com.vaibhav.model.ws

import com.vaibhav.model.Room
import com.vaibhav.util.Constants.TYPE_PHASE_CHANGE
import kotlinx.serialization.Serializable

@Serializable
data class PhaseChange(
    var phase: Room.Phase?,
    var time: Long
): BaseModel(TYPE_PHASE_CHANGE)
