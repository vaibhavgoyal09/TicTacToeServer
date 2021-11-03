package com.vaibhav.routes.websockets

import com.vaibhav.SocketConnection
import com.vaibhav.model.Player
import com.vaibhav.model.ws.BaseModel
import com.vaibhav.model.ws.GameError
import com.vaibhav.model.ws.JoinRoom
import com.vaibhav.session.TicTacToeGameSession
import com.vaibhav.util.Constants.TYPE_JOIN_ROOM
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

@OptIn(ExperimentalSerializationApi::class)
fun Route.gameSocketRoute(
    connection: SocketConnection
) {
    route("/v1/game") {
        standardWebSocket { socket, clientId, frameTextReceived, payload ->
            when (payload) {
                is JoinRoom -> {
                    val room = connection.rooms[payload.roomName]
                    if (room == null) {
                        val gameError = GameError(GameError.TYPE_ROOM_NOT_FOUND)
                        socket.send(Frame.Text(Json.encodeToString(gameError)))
                        return@standardWebSocket
                    }
                    val player = Player(
                        payload.userName,
                        socket,
                        payload.clientId
                    )
                    if (room.containsPlayer(player.userName)) {
                        val playerInRoom = room.players.find { it.clientId == clientId }
                        playerInRoom?.socket = socket
                    } else {

                    }
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
        val session = call.sessions.get<TicTacToeGameSession>() ?: kotlin.run {
            close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "No session."))
            return@webSocket
        }

        try {
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val frameTextReceived = frame.readText()
                    val jsonElement = Json.parseToJsonElement(frameTextReceived)
                    val payload: BaseModel = when (jsonElement.jsonObject["type"].toString()) {
                        TYPE_JOIN_ROOM -> Json.decodeFromJsonElement<JoinRoom>(jsonElement)
                        else -> Json.decodeFromJsonElement(jsonElement)
                    }
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