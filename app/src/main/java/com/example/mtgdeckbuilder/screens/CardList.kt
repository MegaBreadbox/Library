package com.example.mtgdeckbuilder.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.mtgdeckbuilder.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mtgdeckbuilder.ViewModelProvider
import com.example.mtgdeckbuilder.ui.theme.MTGDeckBuilderTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentCardList(
    searchNavigation: () -> Unit,
    navigateBack: () -> Unit,
    cardListViewModel: CardListViewModel = viewModel(factory = ViewModelProvider.Factory)
) {
    val deckState by cardListViewModel.deckUiState.collectAsState()
    val cardList by cardListViewModel.cardListUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            SettingsTopBar(
                isMenuEnabled = cardListViewModel.isMenuEnabled,
                isEditEnabled = cardListViewModel.isEditEnabled,
                isDeleteEnabled = cardListViewModel.isDeleteCardEnabled,
                onMenuClick = { cardListViewModel.onMenuClick() },
                onEditClick = { cardListViewModel.onEditClick() },
                onDeleteCardClick = { cardListViewModel.onDeleteCardClick() },
                onDeleteDeckClick = { cardListViewModel.onDeleteDeckClick() },
                deckName = deckState.name,
                changeDeckName = {
                    coroutineScope.launch {
                        cardListViewModel.updateName(it)
                    }
                },
                modifier = Modifier.padding(dimensionResource(R.dimen.small_padding))
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { searchNavigation() }) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.search_for_new_cards)
                )
                Text(stringResource(R.string.search_cards))
            }
        }
    ) {
        if (cardListViewModel.isDeleteDeckEnabled) {
            DeleteAlertDialog(
                navigateBack = { navigateBack() },
                onDeleteDeckClick = { cardListViewModel.onDeleteDeckClick() },
                confirmDeckDelete = {
                    coroutineScope.launch {
                        cardListViewModel.deleteDeck()
                    }
                }
            )
        }
        Row(
            modifier = Modifier.padding(it)
        ) {
            CardList(
                cardList = cardList
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    isMenuEnabled: Boolean,
    isEditEnabled: Boolean,
    isDeleteEnabled: Boolean,
    deckName: String,
    onMenuClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteCardClick: () -> Unit,
    onDeleteDeckClick: () -> Unit,
    changeDeckName: (String) -> Unit,
    modifier: Modifier = Modifier
){ Column(
    modifier = modifier
    ) {
        TopAppBar(
            title = {
                OutlinedTextField(
                    value = deckName,
                    onValueChange = { changeDeckName(it) },
                    singleLine = true,
                    modifier = modifier
                )
            },
            navigationIcon = {
                Box {
                    IconButton(
                        onClick = { onEditClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = stringResource(R.string.edit_deck_color),
                        )
                    }
                    DropdownMenu(
                        expanded = isEditEnabled,
                        onDismissRequest = { onEditClick() }
                    ) {
                        DropdownMenuItem(text = {  }, onClick = { /*TODO*/ })
                    }
                }
            },
            actions = {
                Box {
                    IconButton(
                        onClick = { onMenuClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = stringResource(R.string.edit_deck_color),
                        )
                    }
                    DropdownMenu(
                        expanded = isMenuEnabled,
                        onDismissRequest = { onMenuClick() },
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.delete_cards))},
                            onClick = {
                                onDeleteCardClick()
                                onMenuClick()
                            },
                            leadingIcon = {
                                if(isDeleteEnabled) {
                                    Icon(Icons.Rounded.Check, contentDescription = null)
                                }
                            }
                        )
                        DropdownMenuItem(text = {
                            Text(
                                text = stringResource(R.string.delete_deck),
                                textAlign = TextAlign.Right,
                                modifier = modifier.fillMaxSize()
                            )
                        },
                            onClick = {
                                onDeleteDeckClick()
                                onMenuClick()
                            }
                        )
                    }
                }

            },
            windowInsets = WindowInsets(0, 0, 0, 0),
        )
        Spacer(modifier = modifier.padding(top = dimensionResource(R.dimen.small_padding)))
        Divider(modifier.padding(dimensionResource(R.dimen.small_padding)))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardList(
    cardList: List<ViewCard>,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(120.dp),
    ){
        items(cardList, key = {it.cardId}) {
            it.imagePng?.let { viewCard -> CardEntry(viewCard) }
        }
    }
}

@Composable
fun CardEntry(
    tradingCardImage: String,
) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(tradingCardImage)
            .build(),
        contentDescription = null,
    )
}

@Composable
fun DeleteAlertDialog(
    navigateBack: () -> Unit,
    onDeleteDeckClick: () -> Unit,
    confirmDeckDelete: () -> Unit
){
    AlertDialog(
        text = { Text(stringResource(R.string.delete_deck_confirmation)) },
        onDismissRequest = { onDeleteDeckClick() },
        confirmButton = {
            TextButton(
                onClick = {
                    confirmDeckDelete()
                    navigateBack()
                }
            ){
                Text(stringResource(R.string.confirm))
            }
        }
    )
}
@Preview
@Composable
fun ScaffoldPreview(){
    MTGDeckBuilderTheme {
        CurrentCardList(
            searchNavigation = {},
            navigateBack = {}
        )
    }
}