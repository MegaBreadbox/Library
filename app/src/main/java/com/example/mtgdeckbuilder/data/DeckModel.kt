package com.example.mtgdeckbuilder.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.mtgdeckbuilder.network.Card
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val deckId: Int = 0,
    val name: String,
    val deckBoxColor: Int
)

@Entity
data class DatabaseCard(
    @PrimaryKey(autoGenerate = true)
    val cardId: Int = 0,
    val imageNormal: String?,
    val imageLarge: String?,
    val imagePng: String?,
    val cmc: Float?,
    val colorIdentity: String
)

@Entity(primaryKeys = ["deckId", "cardId"])
data class DeckCardCrossRef(
    val deckId: Int,
    val cardId: Int
)

data class DeckWithCards(
    @Embedded val deck: Deck,
    @Relation(
        parentColumn = "deckId",
        entityColumn = "cardId",
        associateBy = Junction(DeckCardCrossRef::class)
    )
    val cardList: List<DatabaseCard>
)
