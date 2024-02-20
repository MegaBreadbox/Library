package com.example.mtgdeckbuilder.data

import kotlinx.coroutines.flow.Flow

interface SelectedDeckRepository {
    fun readDeck(): Flow<Int>

    suspend fun changeSelectedDeck(deckKey: Int)
}