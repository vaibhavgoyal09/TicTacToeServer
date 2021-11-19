package com.vaibhav

import com.vaibhav.model.Player
import com.vaibhav.model.Room
import java.util.concurrent.ConcurrentHashMap

class SocketConnection {

    val rooms = ConcurrentHashMap<String, Room>()

    val players = ConcurrentHashMap<String, Player>()

    fun playerJoined(player: Player) {
        players[player.clientId] = player
    }

    fun getRoomWithClientId(clientId: String): Room? {
        val filteredRooms = rooms.filterValues { room ->
            room.players.find { player ->
                player.clientId == clientId
            } != null
        }
        return if (filteredRooms.values.isEmpty()) {
            null
        } else {
            filteredRooms.values.toList()[0]
        }
    }
}