package com.vaibhav.service

import com.vaibhav.model.Room
import com.vaibhav.model.request.CreateRoomRequest
import com.vaibhav.model.response.BasicApiResponse
import com.vaibhav.model.response.RoomResponse
import com.vaibhav.socketConnection
import com.vaibhav.util.ResultHelper

class RoomService() {

    fun createRoom(request: CreateRoomRequest?): ResultHelper<BasicApiResponse> {

        if (request == null) {
            return ResultHelper.Failure("Create room request is null")
        }

        val roomName = request.roomName

        if (socketConnection.rooms[roomName] != null) {
            return ResultHelper.Success(BasicApiResponse(message = "Room with this name already exists"))
        }

        val room = Room(name = roomName)
        socketConnection.rooms[roomName] = room

        return ResultHelper.Success(BasicApiResponse(isSuccessful = true))
    }

    fun joinRoom(userName: String?, roomName: String?): ResultHelper<BasicApiResponse> {

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
                ResultHelper.Success(BasicApiResponse(message = "Room not found"))
            }
            room.containsPlayer(userName!!) -> {
                ResultHelper.Success(BasicApiResponse(message = "A Player with this name has already joined"))
            }
            room.players.size >= 2 -> {
                ResultHelper.Success(BasicApiResponse(message = "Room already full"))
            }
            else -> ResultHelper.Success(BasicApiResponse(isSuccessful = true))
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