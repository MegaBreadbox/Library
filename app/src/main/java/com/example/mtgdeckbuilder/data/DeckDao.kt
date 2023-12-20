package com.example.mtgdeckbuilder.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mtgdeckbuilder.network.Card
import com.example.mtgdeckbuilder.network.CardList

@Dao
interface DeckDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDeck(deck: Deck)

    @Delete
    suspend fun removeDeck(deck: Deck)

    @Query("UPDATE Deck SET cards = :cards WHERE id = :id")
    suspend fun updateDeck(cards: List<Card>, id: Int)

}