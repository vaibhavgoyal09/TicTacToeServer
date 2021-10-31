package com.vaibhav.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val userName: String,
    val clientId: String
)
