package com.vaibhav.routes.websockets

import com.google.gson.JsonParser
import com.vaibhav.gson
import com.vaibhav.model.Player
import com.vaibhav.model.ws.BaseModel
import com.vaibhav.model.ws.GameError
import com.vaibhav.model.ws.JoinRoom
import com.vaibhav.session.TicTacToeGameSession
import com.vaibhav.socketConnection
import com.vaibhav.util.Constants.TYPE_JOIN_ROOM
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.gameSocketRoute() {

    standardWebSocket { socket, clientId, frameTextReceived, payload ->
        when (payload) {
            is JoinRoom -> {
                val room = socketConnection.rooms[payload.roomName]
                if (room == null) {
                    val gameError = GameError(GameError.TYPE_ROOM_NOT_FOUND)
                    socket.send(Frame.Text(gson.toJson(gameError)))
                    return@standardWebSocket
                }
                val player = Player(
                    payload.userName,
                    socket,
                    payload.clientId
                )
                socketConnection.playerJoined(player)
                if (room.containsPlayer(player.userName)) {
                    val playerInRoom = room.players.find { it.clientId == clientId }
                    playerInRoom?.socket = socket
                } else {
                    room.addPlayer(player)
                }
            }
        }
    }
}

fun Route.standardWebSocket(
    handleFrame: suspend (
        socket: DefaultWebSocketServerSession,
        clientId: String,
        frameTextReceived: String,
        payload: BaseModel
    ) -> Unit
) {
    webSocket(path = "/v1/game") {
        val session = call.sessions.get<TicTacToeGameSession>() ?: kotlin.run {
            close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "No session."))
            return@webSocket
        }

        println("web socket connection opened")

        try {
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val frameTextReceived = frame.readText()
                    val jsonObject = JsonParser.parseString(frameTextReceived).asJsonObject
                    val type = when (jsonObject.get("type").asString) {
                        TYPE_JOIN_ROOM -> JoinRoom::class.java
                        else -> BaseModel::class.java
                    }
                    val payload = gson.fromJson(jsonObject, type)
                    handleFrame(this, session.clientId, frameTextReceived, payload)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Handle Socket Closed
        }
    }
}