package com.example.mtgdeckbuilder.screens

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Entity
import com.example.mtgdeckbuilder.R
import com.example.mtgdeckbuilder.data.Deck
import com.example.mtgdeckbuilder.data.DeckRepository
import com.example.mtgdeckbuilder.data.OfflineDeckRepository
import com.example.mtgdeckbuilder.network.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

class DeckListViewModel(private val deckRepository: DeckRepository): ViewModel() {
    private val defaultDeck = Deck(
        name = "Deck",
        deckBoxColor = R.drawable.deckbox_grey
    )

    val deckListUiState: StateFlow<DeckListUiState> =
        deckRepository.getDeckListStream().map { DeckListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DeckListUiState()
            )

    suspend fun createDeck() {
        deckRepository.createDeck(
            name = defaultDeck.name,
            deckBoxColor = defaultDeck.deckBoxColor
        )
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5000L
    }
}

data class DeckListUiState(
    val deckList: List<Deck> = listOf()
)

