package com.vaibhav.routes.http

import com.vaibhav.model.request.CreateRoomRequest
import com.vaibhav.service.RoomService
import com.vaibhav.util.Constants.API_VERSION
import com.vaibhav.util.ResultHelper
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

private const val CREATE_ROOM_ROUTE = "/$API_VERSION/room/create"
private const val JOIN_ROOM_ROUTE = "/$API_VERSION/room/join"
private const val SEARCH_ROOMS_ROUTE = "/$API_VERSION/room/search"

@KtorExperimentalLocationsAPI
@Location(CREATE_ROOM_ROUTE)
class CreateRoomRoute

@KtorExperimentalLocationsAPI
@Location(JOIN_ROOM_ROUTE)
class JoinRoomRoute

@KtorExperimentalLocationsAPI
@Location(SEARCH_ROOMS_ROUTE)
class SearchRoomRoute

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.roomRoutes(
    roomService: RoomService
) {
    post<CreateRoomRoute> {
        val createRoomRequest = call.receiveOrNull<CreateRoomRequest>()
        when (val result = roomService.createRoom(createRoomRequest)) {
            is ResultHelper.Success -> {
                call.respond(
                    HttpStatusCode.OK
                )
            }
            is ResultHelper.Failure -> {
                call.respond(
                    HttpStatusCode.BadRequest,
                    result.errorMessage ?: ""
                )
            }
        }
    }

    get<JoinRoomRoute> {
        val userName = call.request.queryParameters["userName"] ?: kotlin.run {
            call.respond(
                HttpStatusCode.BadRequest,
                "User name is null"
            )
            return@get
        }
        val roomName = call.request.queryParameters["roomName"] ?: kotlin.run {
            call.respond(
                HttpStatusCode.BadRequest,
                "Room name is null"
            )
            return@get
        }

        when (val result = roomService.joinRoom(userName, roomName)) {
            is ResultHelper.Success -> {
                call.respond(
                    HttpStatusCode.OK
                )
            }
            is ResultHelper.Failure -> {
                call.respond(
                    HttpStatusCode.BadRequest,
                    result.errorMessage ?: ""
                )
            }
        }
    }

    get<SearchRoomRoute> {
        val searchQuery = call.request.queryParameters["searchQuery"] ?: ""

        val roomsResponse = roomService.searchRooms(searchQuery)

        call.respond(
            HttpStatusCode.OK,
            roomsResponse
        )
    }
}

