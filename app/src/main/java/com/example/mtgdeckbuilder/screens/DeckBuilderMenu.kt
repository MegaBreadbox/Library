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

enum class CardListScreen() {
    Deck,
    Search,
    Details
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckBuilderMenu(
    navController: NavHostController = rememberNavController(),
    searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)
) {
    Scaffold() {
        padding ->
        NavHost(
            navController = navController,
            startDestination = CardListScreen.Search.name,
            modifier = Modifier.padding(padding)
        ){
            composable(CardListScreen.Search.name) {
                searchScreen(
                    searchViewModel = searchViewModel,
                    onKeyboardSearch = { searchViewModel.initializeCardList(searchViewModel.userText) },
                    detailNavigation = {}
                )
            }
        }
    }
}