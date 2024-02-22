package com.example.mtgdeckbuilder.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mtgdeckbuilder.network.Card
import com.example.mtgdeckbuilder.network.CardList
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDeck(deck: Deck): Long

    @Delete
    suspend fun removeDeck(deck: Deck)

    @Update
    suspend fun updateName(deck: Deck)

    @Query("INSERT INTO Deck (name, deckBoxColor) VALUES (:name || ' ' || (SELECT COUNT(*) + 1 FROM Deck), :deckBoxColor)")
    suspend fun createDeck(name: String, deckBoxColor: Int)

    @Query("SELECT * FROM Deck ORDER BY name ASC")
    fun getDeckList(): Flow<List<Deck>>
    @Transaction
    @Query("SELECT * FROM Deck")
    fun getDeckWithCards(): Flow<List<DeckWithCards>>

}