package com.example.mtgdeckbuilder.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mtgdeckbuilder.network.Card

@Composable
fun detailsScreen(
    card: Card
){
    cardDetails(
        card = card
    )
}

@Composable
fun cardDetails(
    card: Card
){
    Column(){
        Card(){
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(card.imageUris?.png)
                    .build(),
                contentDescription = null,
            )
        }
    }
}
