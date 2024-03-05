package com.example.mtgdeckbuilder.screens

import android.graphics.Color
import android.widget.Button
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.mtgdeckbuilder.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
    cardListViewModel: CardListViewModel = viewModel(factory = ViewModelProvider.Factory)
) {
    val deckState by cardListViewModel.deckUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            SettingsTopBar(
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
        Row(
            modifier = Modifier.padding(it)
        ) {
            CardList()
            Text("Sample")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    deckName: String,
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
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = stringResource(R.string.edit_deck_color),
                    modifier = modifier.clickable { "To Do" }
                )
            },
            actions = {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = stringResource(R.string.menu),
                    modifier = modifier.clickable { "To Do" }
                )
            },
            windowInsets = WindowInsets(0, 0, 0, 0),
        )
        Spacer(modifier = modifier.padding(top = dimensionResource(R.dimen.small_padding)))
        Divider(modifier.padding(dimensionResource(R.dimen.small_padding)))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardList() {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(dimensionResource(R.dimen.card_image)),
    ){

    }
}

@Composable
fun CardEntry(
    tradingCardImage: String
) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(tradingCardImage)
            .build(),
        contentDescription = null
    )
}
@Preview
@Composable
fun ScaffoldPreview(){
    MTGDeckBuilderTheme {
        CurrentCardList(
            searchNavigation = {}
        )
    }
}