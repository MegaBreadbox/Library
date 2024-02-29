package com.example.mtgdeckbuilder.screens

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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
    fun updateAmountToAdd(amount: Int) {
        amountToAdd = amount
    }

    fun changeMenuState(){
        menuState = !menuState
    }

    fun decodeFromString(cardString: String): TradingCard {
        return Json.decodeFromString(cardString)
    }
    private suspend fun createCard(tradingCard: TradingCard, copiesNeeded: Int): List<Long> {
        val databaseCardList: MutableList<DatabaseCard> = mutableListOf()
        repeat(copiesNeeded) {
            databaseCardList.add(tradingCard.tradingCardToDatabaseCard())
        }
        return deckRepository.createCard(databaseCardList.toList())
    }

    suspend fun maxCardsAllowed(tradingCardName: String, scryfallId: String) {
        withContext(ioDispatcher) {
            selectedDeckRepository.readDeck().collect() { deckId ->
                when (scryfallId) {
                    FOREST -> _maxLegalCards.update { null }
                    else -> _maxLegalCards.update {
                        LEGAL_CARD_LIMIT - deckRepository.getTotalCardCopies(
                            name = tradingCardName,
                            deckId = deckId
                        )
                    }
                }
            }
        }
    }

    private suspend fun addCardToDeckList(cardIdList: List<Long>) {
        withContext(ioDispatcher) {
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
        repeat(copiesNeeded) {
            addCardToDeckList(createCard(tradingCard, copiesNeeded))
        }
    }

    companion object {
        const val LEGAL_CARD_LIMIT = 4
        const val FOREST = "ff4c78b4-7178-4a60-ba22-086fb18146df"
    }
}

fun TradingCard.tradingCardToDatabaseCard(): DatabaseCard {
    return DatabaseCard(
        scryfallId = this.scryfallId,
        name = this.name,
        imageNormal = this.imageUris?.normal,
        imageLarge = this.imageUris?.large,
        imagePng = this.imageUris?.png,
        cmc = this.cmc,
        colorIdentity = this.colorIndentity
    )

}
