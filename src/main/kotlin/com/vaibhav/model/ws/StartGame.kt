package com.vaibhav.model.ws

import com.vaibhav.util.Constants.TYPE_START_GAME

data class StartGame(
    val playerWithSymbolXClientId: String,
    val playerWithSymbolOClientId: String
): BaseModel(TYPE_START_GAME)
