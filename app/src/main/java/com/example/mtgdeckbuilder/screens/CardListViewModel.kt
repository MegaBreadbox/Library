package com.example.mtgdeckbuilder.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mtgdeckbuilder.R
import com.example.mtgdeckbuilder.data.Deck
import com.example.mtgdeckbuilder.data.DeckRepository
import com.example.mtgdeckbuilder.data.SelectedDeckRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardListViewModel(
    private val deckRepository: DeckRepository,
    private val selectedDeckRepository: SelectedDeckRepository,
): ViewModel() {

    private val _deckUiState = MutableStateFlow(DeckUiState())
    val deckUiState = _deckUiState.asStateFlow()
    init {
        viewModelScope.launch {
            _deckUiState.update {
                deckRepository.getDeck(
                    selectedDeckRepository.readDeck().first()
                ).first().toUiState()
            }
        }
    }

    suspend fun updateName(name: String) {
        _deckUiState.update {
            it.copy(
                name = name
            )
        }
        deckRepository.updateName(_deckUiState.value.toDeck())
    }



}
data class DeckUiState(
    val deckId: Int = 0,
    val name: String = "",
    val deckBoxColor: Int = R.drawable.deckbox_grey

)

fun Deck.toUiState(): DeckUiState =
     DeckUiState(
        deckId = this.deckId,
        name = this.name,
        deckBoxColor = this.deckBoxColor
    )
fun DeckUiState.toDeck(): Deck =
    Deck(
        deckId = this.deckId,
        name = this.name,
        deckBoxColor = this.deckBoxColor
    )