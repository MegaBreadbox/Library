package com.example.mtgdeckbuilder.data

import com.example.mtgdeckbuilder.network.CardApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val cardListRepository: CardListRepository
}

class DefaultAppContainer : AppContainer {

    private val customJson = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val baseUrl = "https://api.scryfall.com"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(customJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService : CardApiService by lazy {
        retrofit.create(CardApiService::class.java)
    }

    override val cardListRepository: CardListRepository by lazy {
        NetworkCardListRepository(retrofitService)
    }
}