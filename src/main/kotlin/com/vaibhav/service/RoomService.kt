package com.vaibhav.service

import com.vaibhav.SocketConnection
import com.vaibhav.model.Room
import com.vaibhav.model.request.CreateRoomRequest
import com.vaibhav.model.response.RoomResponse
import com.vaibhav.util.ResultHelper

class RoomService(private val socketConnection: SocketConnection) {

    fun createRoom(request: CreateRoomRequest?): ResultHelper<Unit> {

        if (request == null) {
            return ResultHelper.Failure("Create room request is null")
        }

        val roomName = request.roomName

        if (socketConnection.rooms[roomName] != null) {
            return ResultHelper.Failure("Room with this name already exists")
        }

        val room = Room(name = roomName)
        socketConnection.rooms[roomName] = room

        return ResultHelper.Success(Unit)
    }

    fun joinRoom(userName: String?, roomName: String?): ResultHelper<Unit> {

        when {
            userName == null && roomName == null -> {
                return ResultHelper.Failure("Both User name and room name are null.")
            }
            userName == null -> {
                return ResultHelper.Failure("User name is null.")
            }
            roomName == null -> {
                return ResultHelper.Failure("Room name is null.")
            }
        }

        val room = socketConnection.rooms[roomName]

        return when {
            room == null -> {
               ResultHelper.Failure("Room not found")
            }
            room.containsPlayer(userName!!) -> {
                ResultHelper.Failure("A Player with this name has already joined")
            }
            room.players.size >= 2 -> {
                ResultHelper.Failure("Room already full")
            }
            else -> ResultHelper.Success(Unit)
        }
    }

    fun searchRooms(searchQuery: String): List<RoomResponse> {
        val rooms = socketConnection.rooms.filterKeys {
            it.contains(searchQuery, ignoreCase = true)
        }

        return rooms.values.map {
            RoomResponse(
                it.name,
                it.players.size
            )
        }
    }
}