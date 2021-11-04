package com.vaibhav.model

import com.vaibhav.model.ws.PlayerEvents
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.isActive
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(DelicateCoroutinesApi::class, ExperimentalSerializationApi::class)
class Room(
    val name: String,
    var players: List<Player> = listOf()
) {

    private var phaseChangedListener: ((Phase) -> Unit)? = null
    var phase = Phase.WAITING_FOR_PLAYERS
        set(value) {
            synchronized(field) {
                field = value
                phaseChangedListener?.let { change ->
                    change(value)
                }
            }
        }

    fun containsPlayer(userName: String): Boolean {
        return players.find { it.userName == userName } != null
    }

    suspend fun addPlayer(player: Player) {
        println("Player joined ${player.userName}")
        if (players.size == 2) {
            return
        }
        val tempList = players.toMutableList()
        tempList.add(player)
        players = tempList.toList()

        if (players.size == 1) {
            phase = Phase.WAITING_FOR_PLAYERS
        } else if (players.size == 2 && phase == Phase.WAITING_FOR_PLAYERS) {
            phase = Phase.NEW_ROUND
            players = players.shuffled()
        }

        val playerEvent = PlayerEvents(player.clientId, PlayerEvents.TYPE_PLAYER_JOINED)
        broadcastToAllExcept(Json.encodeToString(playerEvent), player.clientId)
    }

    suspend fun broadcastToAllExcept(message: String, clientId: String) {
        players.forEach {
            if (it.clientId != clientId && it.socket.isActive) {
                it.socket.send(Frame.Text(message))
            }
        }
    }

    enum class Phase {
        WAITING_FOR_PLAYERS,
        NEW_ROUND,
        GAME_RUNNING
    }
}