package com.example.mtgdeckbuilder.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.mtgdeckbuilder.R

@Composable
fun searchScreen(
    searchViewModel: SearchViewModel
) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchBar(
    searchViewModel: SearchViewModel,
    onKeyboardSearch: () -> Unit,
) {
    Column(){
        Text(text = stringResource(R.string.search_for_cards))
        TextField(
            value = searchViewModel.userText,
            singleLine = true,
            onValueChange = { input -> searchViewModel.updateUserText(input)},
            label = { Text(stringResource(R.string.search))},
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onKeyboardSearch() }
            )
        )
    }
}

@Composable
fun cardList() {

}

@Composable
fun cardEntry() {

}