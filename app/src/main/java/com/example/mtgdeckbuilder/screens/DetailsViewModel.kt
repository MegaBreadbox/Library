package com.example.mtgdeckbuilder.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mtgdeckbuilder.data.DatabaseCard
import com.example.mtgdeckbuilder.data.DeckCardCrossRef
import com.example.mtgdeckbuilder.data.DeckRepository
import com.example.mtgdeckbuilder.data.DeckWithCards
import com.example.mtgdeckbuilder.data.SelectedDeckRepository
import com.example.mtgdeckbuilder.network.TradingCard
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class DetailsViewModel(
    private val selectedDeckRepository: SelectedDeckRepository,
    private val deckRepository: DeckRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {
    private val _maxLegalCards: MutableStateFlow<Int?> = MutableStateFlow(LEGAL_CARD_LIMIT)
    val maxLegalCards = _maxLegalCards.asStateFlow()

    var amountToAdd by mutableStateOf(0)
        private set

    var menuState by mutableStateOf(false)
       private set

    var unrestrictedCardNumber by mutableStateOf("")
        private set

    fun updateUnrestrictedCardNumber(amount: String) {
        unrestrictedCardNumber = amount.filter { it.isDigit() }
        if(unrestrictedCardNumber.isNotEmpty())
            amountToAdd = unrestrictedCardNumber.toInt()
    }
    fun updateAmountToAdd(amount: Int) {
        amountToAdd = amount
    }

    fun changeMenuState() {
        menuState = !menuState
    }

    fun decodeFromString(cardString: String): TradingCard {
        return Json.decodeFromString(cardString)
    }
    private suspend fun createCard(tradingCard: TradingCard, copiesNeeded: Int): List<Long> {
        val databaseCardList: MutableList<DatabaseCard> = mutableListOf()
        repeat(copiesNeeded) {
            databaseCardList.add(
                tradingCard.tradingCardToDatabaseCard(selectedDeckRepository.readDeck().first())
            )
        }
        return deckRepository.createCard(databaseCardList.toList())
    }

    suspend fun maxCardsAllowed(scryfallId: String) {
        viewModelScope.launch (ioDispatcher) {
            selectedDeckRepository.readDeck().collect { deckId ->
                if (scryfallId in UnrestrictedCardNumber.entries.map { it.scryfallId}) {
                    _maxLegalCards.update { null }
                } else {
                    _maxLegalCards.update {
                        LEGAL_CARD_LIMIT.minus(deckRepository.getDeckWithCards(deckId)
                            .first()
                            .flatMap {
                                it.cardList.filter { databaseCard ->
                                    databaseCard.scryfallId == scryfallId
                                }
                            }.size
                        )
                    }
                }
            }
        }
    }

    private suspend fun addCardToDeckList(cardIdList: List<Long>) {
        viewModelScope.launch(ioDispatcher) {
            selectedDeckRepository.readDeck().collect() { deckId ->
                val deckIdList = mutableListOf<Int>()
                repeat(cardIdList.size) {
                    deckIdList.add(deckId)
                }
                val deckCardCrossRefList = mutableListOf<DeckCardCrossRef>()
                repeat(cardIdList.size) {
                    deckCardCrossRefList.add(
                        DeckCardCrossRef(
                            deckId = deckIdList[it],
                            cardId = cardIdList[it].toInt()
                        )
                    )
                }
                deckRepository.addDeckCardsCrossRef(deckCardCrossRefList.toList())
            }
        }
    }

    suspend fun addCardsToDeck(tradingCard: TradingCard, copiesNeeded: Int) {
        try {
            addCardToDeckList(createCard(tradingCard, copiesNeeded))
        }
        catch(e: Exception) { Log.d("Error", "$e")}
    }
    companion object {
        const val LEGAL_CARD_LIMIT = 4
    }
}
enum class UnrestrictedCardNumber(val scryfallId: String) {
    SHADOWBORN_APOSTLE("ae7ba1de-48f9-423b-867a-22bd3f6c06c2"),
    RELENTLESS_RATS("75f47b6e-9557-4853-b8d6-7602a91c59a7"),
    FOREST("ff4c78b4-7178-4a60-ba22-086fb18146df"),
    MOUNTAIN("c1bba8fb-d763-4efa-8db1-e5e81994b5f9"),
    ISLAND("1cb1ac28-ee04-4892-97ea-2cfdebbafcad"),
    SWAMP("c8e3909e-e00a-4855-a0be-b1c538f89cb8"),
    PLAINS("e81ecd4f-4cde-4d8f-a9b7-d7c6098be981"),
    SNOW_COVERED_FOREST("ca17acea-f079-4e53-8176-a2f5c5c408a1"),
    SNOW_COVERED_MOUNTAIN("5474e67c-628f-41b0-aa31-3d85a267265a"),
    SNOW_COVERED_ISLAND("3bfa5ebc-5623-4eec-89ea-dc187489ee4a"),
    SNOW_COVERED_SWAMP("6aa85af8-15f5-4620-8aea-0b45c28372ed"),
    SNOW_COVERED_PLAINS("afd2730f-878e-47ee-ad2a-73f8fa4e0794"),
}

fun TradingCard.tradingCardToDatabaseCard(deckId: Int): DatabaseCard {
    return DatabaseCard(
        scryfallId = this.scryfallId,
        deckNumber = deckId,
        name = this.name,
        imageNormal = this.imageUris?.normal,
        imageLarge = this.imageUris?.large,
        imagePng = this.imageUris?.png,
        cmc = this.cmc,
        colorIdentity = this.colorIndentity
    )

}
