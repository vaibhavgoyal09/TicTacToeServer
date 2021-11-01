package com.vaibhav.model.ws

import kotlinx.serialization.Serializable

@Serializable
abstract class BaseModel(val type: String)