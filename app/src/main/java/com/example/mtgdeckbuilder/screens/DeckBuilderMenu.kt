package com.example.mtgdeckbuilder.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mtgdeckbuilder.ViewModelProvider
import com.example.mtgdeckbuilder.network.Card

enum class CardListScreen() {
    Deck,
    Search,
    Details
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckBuilderMenu(
    navController: NavHostController = rememberNavController(),
    deckListViewModel: DeckListViewModel = viewModel(factory = ViewModelProvider.Factory)
) {
    Scaffold() {
        padding ->
        NavHost(
            navController = navController,
            startDestination = CardListScreen.Deck.name,
            modifier = Modifier.padding(padding)
        ){
            composable(CardListScreen.Deck.name) {
                deckListScreen(
                    deckListViewModel = deckListViewModel,
                )
            }
            composable(CardListScreen.Search.name) {
                searchScreen(
                    detailNavigation = { navController.navigate(CardListScreen.Details.name) },
                )
            }
            composable(CardListScreen.Details.name) {
                detailsScreen()
            }
        }
    }
}