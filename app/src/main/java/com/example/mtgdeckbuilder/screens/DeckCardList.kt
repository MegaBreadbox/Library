package com.example.mtgdeckbuilder.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mtgdeckbuilder.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckCardList(){
    Scaffold(
        topBar = { DeckSettings() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {"TO DO"}
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_card_to_deck)
                )
            }
        }
    ){

    }
}

@Composable
fun DeckSettings(){
    Column {
        Text(text = "TO DO")
    }
}

@Composable
fun CardList() {

}

@Composable
fun Card() {

}