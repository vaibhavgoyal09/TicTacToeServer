package com.vaibhav.routes.websockets

import com.google.gson.JsonParser
import com.vaibhav.gson
import com.vaibhav.model.Player
import com.vaibhav.model.ws.*
import com.vaibhav.session.TicTacToeGameSession
import com.vaibhav.socketConnection
import com.vaibhav.util.Constants.TYPE_DISCONNECT_REQUEST
import com.vaibhav.util.Constants.TYPE_GAME_MOVE
import com.vaibhav.util.Constants.TYPE_JOIN_ROOM
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.gameSocketRoute() {
    route("/v1/game") {
        standardWebSocket { socket, clientId, _, payload ->
            when (payload) {
                is JoinRoom -> {
                    val room = socketConnection.rooms[payload.roomName]
                    if (room == null) {
                        val gameError = GameError(errorType = GameError.TYPE_ROOM_NOT_FOUND)
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
                is GameMove -> {
                    val room = socketConnection.getRoomWithClientId(clientId)
                    if (room == null) {
                        println("Room was null")
                    }
                    room?.handleMoveReceivedFromPlayer(payload.position, clientId)
                }
                is DisconnectRequest -> {
                    socketConnection.playerLeft(clientId)
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
    webSocket {
        val clientId = call.sessions.get<TicTacToeGameSession>()?.clientId ?: kotlin.run {
            close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "ClientId is null"))
            return@webSocket
        }
        println(clientId)

        try {
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val frameTextReceived = frame.readText()
                    val jsonObject = JsonParser.parseString(frameTextReceived).asJsonObject
                    val type = when (jsonObject.get("type").asString) {
                        TYPE_JOIN_ROOM -> JoinRoom::class.java
                        TYPE_GAME_MOVE -> GameMove::class.java
                        TYPE_DISCONNECT_REQUEST -> DisconnectRequest::class.java
                        else -> BaseModel::class.java
                    }
                    val payload = gson.fromJson(frameTextReceived, type)
                    handleFrame(this, clientId, frameTextReceived, payload)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Handle Socket Closed
        }
    }
}