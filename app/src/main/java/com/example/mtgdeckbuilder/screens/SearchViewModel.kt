package com.example.mtgdeckbuilder.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mtgdeckbuilder.CardListApplication
import com.example.mtgdeckbuilder.data.CardListRepository
import com.example.mtgdeckbuilder.network.CardList
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface CardListUiState {
    data class Success(
        val cardList: CardList
    ): CardListUiState
    object Loading : CardListUiState
    object Error : CardListUiState
}
class SearchViewModel(private val cardListRepository: CardListRepository): ViewModel() {
    var cardListUiState : CardListUiState by mutableStateOf(CardListUiState.Loading)
        private set

    var userText by mutableStateOf("")
        private set


    fun initializeCardList(input: String) {
        viewModelScope.launch {
            cardListUiState = try {
                val result = cardListRepository.getCardList(input)
                CardListUiState.Success(cardList = result)
            } catch (e: IOException) {
                CardListUiState.Error
            }
        }
    }

    fun updateUserText(input: String) {
        userText = input
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CardListApplication)
                val cardListRepository = application.container.cardListRepository
                SearchViewModel(cardListRepository = cardListRepository)
            }
        }
    }
}