package com.example.mtgdeckbuilder.data

import com.example.mtgdeckbuilder.network.Card
import kotlinx.coroutines.flow.Flow


interface DeckRepository {
    suspend fun addDeck(deck: Deck): Long

    suspend fun removeDeck(deck: Deck)

    suspend fun updateName(deck: Deck)

    suspend fun createDeck(name: String, deckBoxColor: Int)

    fun getDeckListStream(): Flow<List<Deck>>

    fun getDeckWithCards(): Flow<List<DeckWithCards>>

}