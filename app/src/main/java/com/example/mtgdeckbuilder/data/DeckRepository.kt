package com.example.mtgdeckbuilder.data

import com.example.mtgdeckbuilder.network.CardList
import kotlinx.coroutines.flow.Flow


interface DeckRepository {
    suspend fun addDeckCardsCrossRef(deckCardCrossRefList: List<DeckCardCrossRef>)
    suspend fun removeCard(databaseCard: DatabaseCard)
    suspend fun removeDeckCardsCrossRef(deckCardCrossRef: DeckCardCrossRef)
    suspend fun removeDeck(deck: Deck)

    suspend fun updateDeck(deck: Deck)

    suspend fun createDeck(name: String, deckBoxColor: Int)

    suspend fun createCard(databaseCardList: List<DatabaseCard>): List<Long>

    fun getDeck(deckId: Int): Flow<Deck>

    suspend fun removeAllDeckCardCrossRef(deckId: Int)
    fun getDeckListStream(): Flow<List<Deck>>

    fun getDeckWithCards(deckId: Int): Flow<List<DeckWithCards>>

}