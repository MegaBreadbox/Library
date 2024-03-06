package com.example.mtgdeckbuilder.screens

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mtgdeckbuilder.R
import com.example.mtgdeckbuilder.data.Deck
import com.example.mtgdeckbuilder.data.DeckRepository
import com.example.mtgdeckbuilder.data.SelectedDeckRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DeckListViewModel(
    private val deckRepository: DeckRepository,
    private val selectedDeckRepository: SelectedDeckRepository
): ViewModel() {

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
    fun changeSelectedDeck(id: Int){
        viewModelScope.launch {
            selectedDeckRepository.changeSelectedDeck(id)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5000L
    }
}

data class DeckListUiState(
    val deckList: List<Deck> = listOf()
)
