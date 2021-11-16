package com.vaibhav.model

import com.vaibhav.gson
import com.vaibhav.model.Player.Companion.SYMBOL_O
import com.vaibhav.model.Player.Companion.SYMBOL_X
import com.vaibhav.model.ws.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay

@OptIn(DelicateCoroutinesApi::class)
class Room(
    val name: String,
    var players: List<Player> = listOf()
) {

    private var playerWithTurn: Player? = null

    private var phaseChangedListener: ((GamePhase) -> Unit)? = null
    private var phase = GamePhase.WAITING_FOR_PLAYERS
        set(value) {
            synchronized(field) {
                field = value
                phaseChangedListener?.let { change ->
                    change(value)
                }
            }
        }

    private var movesCounter: Int = 0

    private val gameBoardPositions: Array<Int> = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

    private val winPositions: Array<IntArray> = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )

    fun containsPlayer(userName: String): Boolean {
        return players.find { it.userName == userName } != null
    }

    suspend fun handleMoveReceivedFromPlayer(position: Int, clientId: String) {
        if (movesCounter >= 9) {
            return
        }
        if (position > 9) {
            val gameError = GameError(errorType = GameError.TYPE_INVALID_MOVE)
            broadcastMessageTo(gson.toJson(gameError), clientId)
            return
        }
        if (phase != GamePhase.GAME_RUNNING) {
            return
        }
        val player = players.find { it.clientId == clientId } ?: return

        if (player != playerWithTurn) {
            return
        }
        if (gameBoardPositions[position] != 0) {
            return
        }
        movesCounter++
        gameBoardPositions[position] = playerWithTurn?.symbol!!
        playerWithTurn = players.find { it.symbol != playerWithTurn?.symbol }

        println(gameBoardPositions.asList())

        val boardChanged = GameBoardStateChange(gameBoardPositions.asList())
        broadcastToAll(gson.toJson(boardChanged))

        var isAnyoneWin = false

        for (winPosition in winPositions) {
            if (gameBoardPositions[winPosition[0]] == gameBoardPositions[winPosition[1]] &&
                gameBoardPositions[winPosition[1]] == gameBoardPositions[winPosition[2]] &&
                gameBoardPositions[winPosition[0]] != 0
            ) {
                isAnyoneWin = true
                val xPlayer = players.find { it.symbol == SYMBOL_X }!!
                val oPlayer = players.find { it.symbol == SYMBOL_O }!!
                val winningPlayer = if (gameBoardPositions[winPosition[0]] == SYMBOL_X) xPlayer else oPlayer
                val loosingPlayer = if (winningPlayer == xPlayer) oPlayer else xPlayer

                val winResult = GameResult(GameResult.TYPE_GAME_WON)
                val lostResult = GameResult(GameResult.TYPE_GAME_LOST)
                broadcastMessageTo(gson.toJson(winResult), winningPlayer.clientId)
                broadcastMessageTo(gson.toJson(lostResult), loosingPlayer.clientId)
            }
        }

        if (movesCounter == 9 && !isAnyoneWin) {
            val gameResult = GameResult(GameResult.TYPE_GAME_DRAW)
            broadcastToAll(gson.toJson(gameResult))
        }
    }

    suspend fun addPlayer(player: Player) {
        println("Player joined ${player.userName}")
        if (players.size == 2) {
            return
        }

        val announcement = Announcement(Announcement.TYPE_PLAYER_JOINED, player.userName)
        broadcastToAllExcept(gson.toJson(announcement), player.clientId)

        val tempList = players.toMutableList()
        tempList.add(player)
        players = tempList.toList()

        if (players.size == 1) {
            phase = GamePhase.WAITING_FOR_PLAYERS

            val gamePhaseChange = GamePhaseChange(GamePhase.WAITING_FOR_PLAYERS, System.currentTimeMillis())
            broadcastMessageTo(gson.toJson(gamePhaseChange), player.clientId)

        } else if (players.size == 2 && phase == GamePhase.WAITING_FOR_PLAYERS) {
            phase = GamePhase.WAITING_FOR_START
            players = players.shuffled()

            val playerWithSymbolX = players.first()
            val playerWithSymbolO = players.last()

            val xSymbol = SYMBOL_X
            val oSymbol = SYMBOL_O

            playerWithSymbolO.symbol = oSymbol
            playerWithSymbolO.symbol = xSymbol

            playerWithTurn = playerWithSymbolX

            val phaseChange = GamePhaseChange(phase = phase, System.currentTimeMillis())
            broadcastToAll(gson.toJson(phaseChange))

            delay(DELAY_GAME_START)

            val startGame = StartGame(
                playerWithSymbolX.userName, playerWithSymbolO.userName
            )
            broadcastToAll(gson.toJson(startGame))
            phase = GamePhase.GAME_RUNNING
        }
    }

    private suspend fun broadcastMessageTo(message: String, clientId: String) {
        val player = players.find { it.clientId == clientId }
        player?.socket?.send(Frame.Text(message))
    }

    private suspend fun broadcastToAllExcept(message: String, clientId: String) {
        players.forEach {
            if (it.clientId != clientId) {
                it.socket.send(Frame.Text(message))
            }
        }
    }

    private suspend fun broadcastToAll(message: String) {
        players.forEach {
            it.socket.send(Frame.Text(message))
        }
    }

    companion object {
        const val DELAY_GAME_START = 3000L
    }

    enum class GamePhase {
        WAITING_FOR_PLAYERS,
        NEW_ROUND,
        WAITING_FOR_START,
        GAME_RUNNING
    }
}