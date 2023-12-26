package com.example.mtgdeckbuilder.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mtgdeckbuilder.network.Card
import com.example.mtgdeckbuilder.network.CardList
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDeck(deck: Deck)

    @Delete
    suspend fun removeDeck(deck: Deck)

    @Update
    suspend fun updateName(deck: Deck)

    @Query("UPDATE Deck SET cards = :cards WHERE id = :id")
    fun updateDeck(cards: List<Card>, id: Int): Flow<Deck>

}