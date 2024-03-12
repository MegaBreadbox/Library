package com.example.mtgdeckbuilder.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mtgdeckbuilder.R
import com.example.mtgdeckbuilder.ViewModelProvider
import com.example.mtgdeckbuilder.data.Deck
import com.example.mtgdeckbuilder.ui.theme.MTGDeckBuilderTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListScreen(
    cardListNavigation: () -> Unit,
    modifier: Modifier = Modifier,
    deckListViewModel: DeckListViewModel = viewModel(factory = ViewModelProvider.Factory)
) {
    val deckListUiState by deckListViewModel.deckListUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(){
        Scaffold(
            modifier = modifier,
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                              coroutineScope.launch{
                                  deckListViewModel.createDeck()
                              }
                    },
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = stringResource(R.string.add_deck)
                    )
                    Text(
                        text = stringResource(R.string.add_deck_fab),
                    )
                }
            }
        ) { innerPadding ->
            DeckList(
                deckList = deckListUiState.deckList,
                changeCurrentDeck = {
                    coroutineScope.launch {
                        deckListViewModel.changeSelectedDeck(it)
                        cardListNavigation()
                    }
                },
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun DeckList(
    deckList: List<Deck>,
    changeCurrentDeck: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = dimensionResource(R.dimen.deck_image)),
        contentPadding = PaddingValues(dimensionResource(R.dimen.small_padding)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding))
    ){
        items(deckList) {deck ->
            DeckEntry(deck, { changeCurrentDeck(it) })

        }
    }
}

@Composable
fun DeckEntry(
    deck: Deck,
    changeCurrentDeck: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable {
            changeCurrentDeck(deck.deckId)
        }
    ){Arrangement.Center
        Image(
            painter = painterResource(deck.deckBoxColor),
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.medium_padding))
        )
        Text(
            text = deck.name,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.small_padding))
        )
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MTGDeckBuilderTheme {

    }
}
