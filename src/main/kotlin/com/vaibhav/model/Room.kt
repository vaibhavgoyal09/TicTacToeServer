package com.vaibhav.model

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(DelicateCoroutinesApi::class, ExperimentalSerializationApi::class)
class Room(
    val name: String,
    var players: List<Player> = listOf()
) {

    fun containsPlayer(userName: String): Boolean {
        return players.find { it.userName == userName } != null
    }

    enum class Phase {
        WAITING_FOR_PLAYER,
        NEW_ROUND,

    }
}