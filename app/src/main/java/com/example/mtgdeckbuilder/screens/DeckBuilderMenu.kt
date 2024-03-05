package com.example.mtgdeckbuilder.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

enum class CardListScreen() {
    Deck,
    CardList,
    Search,
    Details
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckBuilderMenu(
    navController: NavHostController = rememberNavController(),
) {
    Scaffold() {
        padding ->
        NavHost(
            navController = navController,
            startDestination = CardListScreen.Deck.name,
            modifier = Modifier.padding(padding)
        ){
            composable(CardListScreen.Deck.name) {
                DeckListScreen(
                    cardListNavigation = { navController.navigate(CardListScreen.CardList.name) }
                )
            }
            composable(CardListScreen.CardList.name) {
                CurrentCardList(
                    searchNavigation = { navController.navigate(CardListScreen.Search.name)}
                )
            }
            composable(CardListScreen.Search.name) {
                SearchScreen(
                    detailNavigation = { navController.navigate(
                         "${CardListScreen.Details.name}/$it") },
                )
            }
            composable(
                route = "${CardListScreen.Details.name}/{card}",
                arguments = listOf(
                    navArgument("card") {
                        type = NavType.StringType
                    }
                )

            ) {
                val card = it.arguments?.getString("card") ?: ""
                DetailsScreen(card)
            }
        }
    }
}