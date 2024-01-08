package com.example.mtgdeckbuilder.data

import com.example.mtgdeckbuilder.network.Card
import kotlinx.coroutines.flow.Flow


interface DeckRepository {
    suspend fun addDeck(deck: Deck)

    suspend fun removeDeck(deck: Deck)

    suspend fun updateName(deck: Deck)

    suspend fun updateDeck(cards: List<Card>, id: Int)

    fun getDeckListStream(): Flow<List<Deck>>

}