package com.vaibhav

import com.vaibhav.model.Player
import com.vaibhav.model.Room
import java.util.concurrent.ConcurrentHashMap

class SocketConnection {

    val rooms = ConcurrentHashMap<String, Room>()

    val players = ConcurrentHashMap<String, Player>()
}