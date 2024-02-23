package com.example.mtgdeckbuilder.data

import kotlinx.coroutines.flow.Flow

class OfflineDeckRepository(private val deckDao: DeckDao): DeckRepository {
    override suspend fun addDeck(deck: Deck): Long {
        return deckDao.addDeck(deck)
    }

    override suspend fun removeDeck(deck: Deck) {
        deckDao.removeDeck(deck)
    }

    override suspend fun updateName(deck: Deck) {
        deckDao.updateName(deck)
    }

    override suspend fun createDeck(name: String, deckBoxColor: Int) {
        deckDao.createDeck(name, deckBoxColor)
    }

    override fun getDeckListStream(): Flow<List<Deck>> {
        return deckDao.getDeckList()
    }

    override fun getDeckWithCards(): Flow<List<DeckWithCards>> {
        return deckDao.getDeckWithCards()
    }

}