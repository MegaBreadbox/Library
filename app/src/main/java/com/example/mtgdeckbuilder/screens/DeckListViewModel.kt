package com.example.mtgdeckbuilder.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mtgdeckbuilder.data.Deck
import com.example.mtgdeckbuilder.data.DeckRepository
import com.example.mtgdeckbuilder.data.OfflineDeckRepository
import com.example.mtgdeckbuilder.network.Card

class DeckListViewModel(private val deckRepository: DeckRepository): ViewModel() {

    var deckUiState: DeckUiState by mutableStateOf(DeckUiState())
        private set
    suspend fun createDeckList() {
        deckRepository.addDeck(deckUiState.toDeck())
    }

    suspend fun updateDeckName(name: String) {
        deckUiState = deckUiState.copy(name = name)
        deckRepository.updateName(deckUiState.toDeck())
    }
}

fun DeckUiState.toDeck(): Deck{
    return Deck(id = id, name = name, cards = cards)
}

data class DeckUiState(
    val id: Int = 0,
    val name: String = "",
    val cards: List<Card> = emptyList()
)