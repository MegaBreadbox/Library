package com.example.mtgdeckbuilder.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val deckId: Int = 0,
    val name: String,
    val deckBoxColor: Int
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Deck::class,
            parentColumns = arrayOf("deckId"),
            childColumns = arrayOf("deckNumber"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DatabaseCard(
    @PrimaryKey(autoGenerate = true)
    val cardId: Int = 0,
    @ColumnInfo(index = true)
    val deckNumber: Int,
    val scryfallId: String,
    val name: String,
    val imageNormal: String?,
    val imageLarge: String?,
    val imagePng: String?,
    val cmc: Float?,
    val colorIdentity: Array<String>
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
