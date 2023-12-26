package com.example.mtgdeckbuilder.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mtgdeckbuilder.network.Card
@Entity
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val cards: List<Card>
)