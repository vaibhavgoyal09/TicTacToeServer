package com.vaibhav.model.response

import kotlinx.serialization.Serializable

@Serializable
data class BasicApiResponse(
    val isSuccessful: Boolean = false,
    val message: String? = null
)
