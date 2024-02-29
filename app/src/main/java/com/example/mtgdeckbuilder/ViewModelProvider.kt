package com.example.mtgdeckbuilder

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mtgdeckbuilder.screens.DeckListViewModel
import com.example.mtgdeckbuilder.screens.DetailsViewModel
import com.example.mtgdeckbuilder.screens.SearchViewModel
import kotlinx.coroutines.Dispatchers

object ViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            SearchViewModel(deckBuilderApplication().container.cardListRepository)
        }

        initializer {
            DeckListViewModel(
                deckBuilderApplication().container.deckRepository,
                deckBuilderApplication().container.selectedDeckRepository
            )
        }

        initializer {
            DetailsViewModel(
                deckBuilderApplication().container.selectedDeckRepository,
                deckBuilderApplication().container.deckRepository,
                Dispatchers.IO
            )
        }

    }
}

fun CreationExtras.deckBuilderApplication(): CardListApplication{
    return (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CardListApplication)
}