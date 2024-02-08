package com.example.mtgdeckbuilder.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mtgdeckbuilder.network.Card
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val deckBoxColor: Int
)

@Entity
data class DatabaseCard(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageNormal: String?,
    val imageLarge: String?,
    val imagePng: String?,
    val cmc: Float?,
    val colorIdentity: String
)


