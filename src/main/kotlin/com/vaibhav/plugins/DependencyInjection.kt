package com.vaibhav.plugins

import com.vaibhav.di.mainModule
import io.ktor.application.*
import org.koin.ktor.ext.Koin

fun Application.configureDI() {
    install(Koin) {
        modules(mainModule)
    }
}