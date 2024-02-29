package com.example.mtgdeckbuilder.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDeckCardsCrossRef(deckCardCrossRefList: List<DeckCardCrossRef>)
    @Delete
    suspend fun removeDeck(deck: Deck)

    @Update
    suspend fun updateName(deck: Deck)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createCard(databaseCardList: List<DatabaseCard>): List<Long>
    @Query("INSERT OR IGNORE INTO Deck (name, deckBoxColor) " +
            "VALUES (:name || ' ' || (SELECT COUNT(*) + 1 FROM Deck), :deckBoxColor)")
    suspend fun createDeck(name: String, deckBoxColor: Int)

    @Query("SELECT * FROM Deck ORDER BY name ASC")
    fun getDeckList(): Flow<List<Deck>>
    @Transaction
    @Query("SELECT * FROM Deck WHERE :deckId = deckId")
    fun getDeckWithCards(deckId: Int): Flow<List<DeckWithCards>>

    @Transaction
    @Query("SELECT COUNT(*) FROM DECK WHERE :name = name AND :deckId = deckId")
    fun getTotalCardCopies(name: String, deckId: Int): Int

}