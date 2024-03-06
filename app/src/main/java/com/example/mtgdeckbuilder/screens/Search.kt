package com.example.mtgdeckbuilder.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mtgdeckbuilder.R
import com.example.mtgdeckbuilder.ViewModelProvider
import com.example.mtgdeckbuilder.network.TradingCard
import com.example.mtgdeckbuilder.network.CardList
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    detailNavigation: (String) -> Unit,
    searchViewModel: SearchViewModel = viewModel(factory = ViewModelProvider.Factory)
) {
    val errorPresent by searchViewModel.errorPresent.collectAsState()
    val searchUiState = searchViewModel.cardListUiState
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        searchBar(
            inputText = searchViewModel.userText,
            onTextChange = { searchViewModel.updateUserText(it) },
            onKeyboardSearch = {
                searchViewModel.initializeCardList(searchViewModel.userText)
                keyboardController?.hide()
            },
            searchError = errorPresent
        )

        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_padding)))

        when (searchUiState) {
            is CardListUiState.Success ->
                cardList(
                    initializePage = { searchViewModel.nextPage() },
                    loadPreviousPage = { searchViewModel.previousPage() },
                    cardList = searchUiState.cardList,
                    currentlyLoading = searchViewModel.loadingImage,
                    pageListSize = searchViewModel.currentListSize(),
                    onClick = { detailNavigation(searchViewModel.cardToJson(it)) },

                )
            is CardListUiState.Error -> null
            is CardListUiState.Loading ->
                Divider(modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.medium_padding),
                    end = dimensionResource(R.dimen.medium_padding)
                    )
                )
            is CardListUiState.NoResults -> NoResultsMessage()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchBar(
    inputText: String,
    searchError: Boolean,
    onTextChange: (String) -> Unit,
    onKeyboardSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ){
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.medium_padding)))
        OutlinedTextField(
            value = inputText,
            singleLine = true,
            isError = searchError,
            onValueChange = { input -> onTextChange(input) },
            label = { Text(stringResource(R.string.search_for_cards)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onKeyboardSearch()
                }
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun cardList(
    initializePage: () -> Unit,
    loadPreviousPage: () -> Unit,
    cardList: CardList,
    currentlyLoading: Boolean,
    pageListSize: Int,
    onClick: (TradingCard) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberLazyStaggeredGridState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()

        ) {
            loadPrevious(
                currentlyLoading = currentlyLoading,
                loadPreviousPage = { loadPreviousPage() },
                resetPosition = { coroutineScope.launch { state.scrollToItem(0) } },
                pageListSize = pageListSize
            )
            Text(
                text = stringResource(R.string.pageNumber, pageListSize),
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.large_padding))
            )
            load(
                currentlyLoading = currentlyLoading,
                initializePage = { initializePage() },
                resetPosition = { coroutineScope.launch { state.scrollToItem(0) } }
            )
        }
        Divider(
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.medium_padding),
                end = dimensionResource(R.dimen.medium_padding),
                top = dimensionResource(R.dimen.small_padding),
                bottom = dimensionResource(R.dimen.small_padding)
            )
        )
        LazyHorizontalStaggeredGrid(
            rows = StaggeredGridCells.Adaptive(minSize = dimensionResource(R.dimen.card_image)),
            state = state,
            modifier = modifier.fillMaxSize()
        ) {
            items(cardList.data) { entry ->
                cardEntry(entry, { onClick(it) })
            }
        }

    }
}

@Composable
fun load(
    currentlyLoading: Boolean,
    initializePage: () -> Unit,
    resetPosition: () -> Unit
) {
    OutlinedButton(
        enabled = !currentlyLoading,
        onClick = { initializePage(); resetPosition(); }
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowForward,
            contentDescription = stringResource(R.string.next_page)
        )
    }
}
@Composable
fun loadPrevious(
    currentlyLoading: Boolean,
    loadPreviousPage: () -> Unit,
    resetPosition: () -> Unit,
    pageListSize: Int
) {
    OutlinedButton(
        enabled = pageListSize > 1 && !currentlyLoading,
        onClick = { loadPreviousPage(); resetPosition() }
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = stringResource(R.string.load_previous)
        )
    }

}

@Composable
fun cardEntry(
    tradingCard: TradingCard,
    onClick: (TradingCard) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable() {
                onClick(tradingCard)
            }
            .padding(
                top = dimensionResource(R.dimen.small_padding),
                end = dimensionResource(R.dimen.small_padding)
            )
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(tradingCard.imageUris?.large)
                .build(),
            contentDescription = null,
        )
    }
}

@Composable
fun NoResultsMessage(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No results",
            color = MaterialTheme.colorScheme.error
        )
    }
}