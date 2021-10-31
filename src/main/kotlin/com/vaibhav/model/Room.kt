package com.vaibhav.model

class Room(
    val name: String,
    val players: List<Player> = listOf()
) {
    fun containsPlayer(userName: String): Boolean {
        return players.find { it.userName == userName } != null
    }
}