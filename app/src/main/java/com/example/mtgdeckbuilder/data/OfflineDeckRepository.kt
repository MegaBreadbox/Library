package com.example.mtgdeckbuilder.data

import com.example.mtgdeckbuilder.network.Card
import kotlinx.coroutines.flow.Flow

class OfflineDeckRepository(private val deckDao: DeckDao): DeckRepository {
    override suspend fun addDeck(deck: Deck) {
        deckDao.addDeck(deck)
    }

    override suspend fun removeDeck(deck: Deck) {
        deckDao.removeDeck(deck)
    }

    override suspend fun updateName(deck: Deck) {
        deckDao.updateName(deck)
    }

    override fun getDeckListStream(): Flow<List<Deck>> {
        return deckDao.getDeckList()
    }

    override fun getDeckWithCards(): Flow<List<DeckWithCards>> {
        return deckDao.getDeckWithCards()
    }

}