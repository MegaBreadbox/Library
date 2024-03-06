package com.example.mtgdeckbuilder.data

import kotlinx.coroutines.flow.Flow

class OfflineDeckRepository(private val deckDao: DeckDao): DeckRepository {
    override suspend fun addDeckCardsCrossRef(deckCardCrossRefList: List<DeckCardCrossRef>) {
       deckDao.addDeckCardsCrossRef(deckCardCrossRefList)
    }

    override suspend fun removeCard(databaseCard: DatabaseCard) {
       deckDao.removeCard(databaseCard)
    }

    override suspend fun removeDeckCardsCrossRef(deckCardCrossRef: DeckCardCrossRef) {
        deckDao.removeDeckCardsCrossRef(deckCardCrossRef)
    }

    override suspend fun removeDeck(deck: Deck) {
        deckDao.removeDeck(deck)
    }

    override suspend fun updateDeck(deck: Deck) {
        deckDao.updateDeck(deck)
    }

    override suspend fun createDeck(name: String, deckBoxColor: Int) {
        deckDao.createDeck(name, deckBoxColor)
    }

    override suspend fun createCard(databaseCardList: List<DatabaseCard>): List<Long> {
        return deckDao.createCard(databaseCardList)
    }

    override fun getDeck(deckId: Int): Flow<Deck> {
        return deckDao.getDeck(deckId)
    }

    override suspend fun removeAllDeckCardCrossRef(deckId: Int) {
        return deckDao.removeAllDeckCardCrossRef(deckId)
    }

    override fun getDeckListStream(): Flow<List<Deck>> {
        return deckDao.getDeckList()
    }

    override fun getDeckWithCards(deckId: Int): Flow<List<DeckWithCards>> {
        return deckDao.getDeckWithCards(deckId)
    }

}