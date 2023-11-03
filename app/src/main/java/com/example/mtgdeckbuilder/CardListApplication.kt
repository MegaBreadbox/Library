package com.example.mtgdeckbuilder

import android.app.Application
import com.example.mtgdeckbuilder.data.AppContainer
import com.example.mtgdeckbuilder.data.DefaultAppContainer

class CardListApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}