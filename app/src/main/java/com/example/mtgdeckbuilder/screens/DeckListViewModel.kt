package com.example.mtgdeckbuilder.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mtgdeckbuilder.R
import com.example.mtgdeckbuilder.data.Deck
import com.example.mtgdeckbuilder.data.DeckRepository
import com.example.mtgdeckbuilder.data.OfflineDeckRepository
import com.example.mtgdeckbuilder.network.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class DeckListViewModel(private val deckRepository: DeckRepository): ViewModel() {

    private val _deckUiState = MutableStateFlow(DeckUiState())
    val deckUiState: StateFlow<DeckUiState> = _deckUiState.asStateFlow()


    val deckListUiState: StateFlow<DeckListUiState> =
        deckRepository.getDeckListStream().map { DeckListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DeckListUiState()
            )


    suspend fun createDeck() {
        deckRepository.addDeck(_deckUiState.value.toDeck())
    }

    suspend fun updateDeckName(name: String) {
        _deckUiState.update{ currentState ->
            currentState.copy(
                name = name
            )

        }
        deckRepository.updateName(_deckUiState.value.toDeck())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5000L
    }
}

fun DeckUiState.toDeck(): Deck{
    return Deck(deckId = id, name = name, deckBoxColor = deckBoxColor)
}

data class DeckListUiState(
    val deckList: List<Deck> = listOf()
)

data class DeckUiState(
    val id: Int = 0,
    val name: String = "",
    val deckBoxColor: Int = R.drawable.deckbox_grey,
)