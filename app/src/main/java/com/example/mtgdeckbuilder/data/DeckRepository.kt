package com.example.mtgdeckbuilder.data

import com.example.mtgdeckbuilder.network.CardList
import kotlinx.coroutines.flow.Flow


interface DeckRepository {
    suspend fun addDeckCardsCrossRef(deckCardCrossRefList: List<DeckCardCrossRef>)
    suspend fun removeDeck(deck: Deck)

    suspend fun updateName(deck: Deck)

    suspend fun createDeck(name: String, deckBoxColor: Int)

    suspend fun createCard(databaseCardList: List<DatabaseCard>): List<Long>

    fun getDeckListStream(): Flow<List<Deck>>

    fun getDeckWithCards(deckId: Int): Flow<List<DeckWithCards>>

    fun getTotalCardCopies(name: String, deckId: Int): Int

}