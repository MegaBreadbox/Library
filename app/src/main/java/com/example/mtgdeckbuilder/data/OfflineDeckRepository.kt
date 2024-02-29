package com.example.mtgdeckbuilder.data

import kotlinx.coroutines.flow.Flow

class OfflineDeckRepository(private val deckDao: DeckDao): DeckRepository {
    override suspend fun addDeckCardsCrossRef(deckCardCrossRefList: List<DeckCardCrossRef>) {
       deckDao.addDeckCardsCrossRef(deckCardCrossRefList)
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

    override suspend fun createCard(databaseCardList: List<DatabaseCard>): List<Long> {
        return deckDao.createCard(databaseCardList)
    }

    override fun getDeckListStream(): Flow<List<Deck>> {
        return deckDao.getDeckList()
    }

    override fun getDeckWithCards(deckId: Int): Flow<List<DeckWithCards>> {
        return deckDao.getDeckWithCards(deckId)
    }

    override fun getTotalCardCopies(name: String, deckId: Int): Int {
        return deckDao.getTotalCardCopies(name, deckId)
    }

}