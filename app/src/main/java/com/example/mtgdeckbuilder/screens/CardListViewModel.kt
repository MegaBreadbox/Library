package com.example.mtgdeckbuilder.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mtgdeckbuilder.R
import com.example.mtgdeckbuilder.data.DatabaseCard
import com.example.mtgdeckbuilder.data.Deck
import com.example.mtgdeckbuilder.data.DeckCardCrossRef
import com.example.mtgdeckbuilder.data.DeckRepository
import com.example.mtgdeckbuilder.data.SelectedDeckRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardListViewModel(
    private val deckRepository: DeckRepository,
    private val selectedDeckRepository: SelectedDeckRepository,
): ViewModel() {

    private val _deckUiState = MutableStateFlow(DeckUiState())
    val deckUiState = _deckUiState.asStateFlow()

    private val deckId: StateFlow<Int> =
        selectedDeckRepository.readDeck().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIBE_DELAY),
            initialValue = 0
        )


    @OptIn(ExperimentalCoroutinesApi::class)
    val cardListUiState: StateFlow<List<ViewCard>> = deckId.flatMapLatest {
        deckRepository.getDeckWithCards(it).map { listOfCards ->
            listOfCards.flatMap { deckWithCards ->
                deckWithCards.cardList.map { dataBaseCard ->
                    dataBaseCard.toViewCard()
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(SUBSCRIBE_DELAY),
        initialValue = listOf(ViewCard())
    )
    var isMenuEnabled by mutableStateOf(false)
        private set

    var isEditEnabled by mutableStateOf(false)
        private set

    var isDeleteCardEnabled by mutableStateOf(false)
        private set

    var isDeleteDeckEnabled by mutableStateOf(false)

    init {
        _deckUiState.update { DeckUiState() }
        viewModelScope.launch {
            selectedDeckRepository.readDeck().collect() { deckId ->
                _deckUiState.update {
                    deckRepository.getDeck(deckId).first().toUiState()
                }
            }
        }
    }

    fun onMenuClick() {
        isMenuEnabled = !isMenuEnabled
    }

    fun onEditClick() {
        isEditEnabled = !isEditEnabled
    }

    fun onDeleteCardClick() {
        isDeleteCardEnabled = !isDeleteCardEnabled
    }

    fun onDeleteDeckClick() {
        isDeleteDeckEnabled = !isDeleteDeckEnabled
    }

    suspend fun updateColor(deckResourceId: Int) {
        _deckUiState.update {
            it.copy(
                deckBoxColor = deckResourceId
            )
        }
        deckRepository.updateDeck(_deckUiState.value.toDeck())
    }
    suspend fun updateName(name: String) {
        if (name.length < 14) {
            _deckUiState.update {
                it.copy(
                    name = name
                )
            }
            deckRepository.updateDeck(_deckUiState.value.toDeck())
        }
    }

    suspend fun deleteCard(viewCard: ViewCard) {
        deckRepository.removeDeckCardsCrossRef(
            DeckCardCrossRef(_deckUiState.value.deckId, viewCard.cardId)
        )
        deckRepository.removeCard(viewCard.toDatabaseCard())
    }

    suspend fun deleteDeck() {
            deckRepository.removeAllDeckCardCrossRef(_deckUiState.value.deckId)
            deckRepository.removeDeck(_deckUiState.value.toDeck())
    }

    companion object {
        private const val SUBSCRIBE_DELAY = 5000L
    }

}

data class ViewCard(
    val cardId: Int = 0,
    val deckNumber: Int = 0,
    val scryfallId: String = "",
    val name: String = "",
    val imageNormal: String? = "",
    val imageLarge: String? = "",
    val imagePng: String? = "",
    val cmc: Float? = 0F,
    val colorIdentity: Array<String> = arrayOf()
)
data class DeckUiState(
    val deckId: Int = 0,
    val name: String = "",
    val deckBoxColor: Int = R.drawable.deck_box

)

fun DatabaseCard.toViewCard(): ViewCard =
    ViewCard(
        this.cardId,
        this.deckNumber,
        this.scryfallId,
        this.name,
        this.imageNormal,
        this.imageLarge,
        this.imagePng,
        this.cmc,
        this.colorIdentity
    )
fun ViewCard.toDatabaseCard(): DatabaseCard =
    DatabaseCard(
        this.cardId,
        this.deckNumber,
        this.scryfallId,
        this.name,
        this.imageNormal,
        this.imageLarge,
        this.imagePng,
        this.cmc,
        this.colorIdentity
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