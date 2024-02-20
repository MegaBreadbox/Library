package com.example.mtgdeckbuilder.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mtgdeckbuilder.network.CardApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val cardListRepository: CardListRepository
    val deckRepository: DeckRepository
    val selectedDeckRepository: SelectedDeckRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    @OptIn(ExperimentalSerializationApi::class)
    private val customJson = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val baseUrl = "https://api.scryfall.com/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(customJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService : CardApiService by lazy {
        retrofit.create(CardApiService::class.java)
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = SELECTED_DECK_NAME
    )

    override val cardListRepository: CardListRepository by lazy {
        NetworkCardListRepository(retrofitService)
    }

    override val deckRepository: DeckRepository by lazy {
        OfflineDeckRepository(DeckDatabase.getDatabase(context).deckDao())
    }

    override val selectedDeckRepository: SelectedDeckRepository by lazy {
        UserSelectedDeck(dataStore = context.dataStore)
    }

    companion object {
        private const val SELECTED_DECK_NAME = "selected_deck"
    }
}