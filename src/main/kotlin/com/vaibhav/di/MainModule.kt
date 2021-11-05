package com.vaibhav.di

import com.vaibhav.service.RoomService
import org.koin.dsl.module

val mainModule = module {

    single {
        RoomService()
    }
}