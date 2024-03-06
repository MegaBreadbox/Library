package com.example.mtgdeckbuilder.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mtgdeckbuilder.data.CardListRepository
import com.example.mtgdeckbuilder.network.TradingCard
import com.example.mtgdeckbuilder.network.CardList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import java.net.URLEncoder

sealed interface CardListUiState {
    data class Success(
        val cardList: CardList
    ): CardListUiState
    object Loading : CardListUiState
    object Error : CardListUiState
    object NoResults : CardListUiState
}
class SearchViewModel(private val cardListRepository: CardListRepository): ViewModel() {
    var cardListUiState : CardListUiState by mutableStateOf(CardListUiState.Loading)
        private set

    private val _errorPresent = MutableStateFlow(false)
    val errorPresent = _errorPresent.asStateFlow()

    var userText by mutableStateOf("")
        private set

    var loadingImage by mutableStateOf(false)
        private set

    private lateinit var currentCardList: CardList
    private lateinit var nextPageCardList: CardList

    private val cardPages: MutableList<CardList> = mutableListOf()

    fun initializeCardList(input: String) {
        viewModelScope.launch {
            cardListUiState = try {
                delay(QUERY_DELAY)
                cardPages.clear()
                currentCardList = cardListRepository.getCardList(input)
                addToList(currentCardList)
                changeErrorState(error = false)
                CardListUiState.Success(cardList = currentCardList)
            } catch (e: IOException) {
                changeErrorState(error = true)
                CardListUiState.Error
            } catch (e: HttpException) {
                changeErrorState(error = true)
                CardListUiState.NoResults
            }
        }
    }

    fun nextPage() {
        viewModelScope.launch {
            cardListUiState = try {
                loadingImage = true
                delay(QUERY_DELAY)
                if(currentCardList.hasMore) {
                    nextPageCardList =
                        cardListRepository.nextPage(currentCardList.nextPage)
                    currentCardList = nextPageCardList
                    addToList(currentCardList)
                    loadingImage = false
                    CardListUiState.Success(cardList = nextPageCardList)
                } else {
                    CardListUiState.Success(cardList = currentCardList)
                }
            } catch (e: IOException) {
                CardListUiState.Error
            }
        }
    }

    fun previousPage() {
        viewModelScope.launch {
            cardListUiState = try {
                delay(QUERY_DELAY)
                if(cardPages.size > 1){
                    removeLatest()
                    currentCardList = cardPages[cardPages.size - 1]
                    CardListUiState.Success(cardList = currentCardList)
                } else {
                    CardListUiState.Success(cardList = currentCardList)
                }
            } catch (e: IOException) {
                CardListUiState.Error
            }
        }
    }
    fun changeErrorState(error: Boolean){
        _errorPresent.update{ error }
    }

    fun cardToJson(input: TradingCard): String {
        return URLEncoder.encode(Json.encodeToString(input), "utf-8")
    }

    fun currentListSize(): Int = cardPages.size


    private fun addToList(cardList: CardList){
        cardPages.add(cardList)
    }

    private fun removeLatest(){
        cardPages.removeAt(cardPages.size - 1)
    }

    fun updateUserText(input: String) {
        userText = input
    }
    companion object{
        const val  QUERY_DELAY = 50L
    }

}