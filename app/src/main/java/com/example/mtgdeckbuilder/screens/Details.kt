package com.example.mtgdeckbuilder.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mtgdeckbuilder.ViewModelProvider
import com.example.mtgdeckbuilder.network.Card
import kotlinx.serialization.json.Json

//TO DO: find a cleaner way to pass card object than reusing search viewModel
@Composable
fun detailsScreen(
    cardString: String
){
    cardDetails(
        card = Json.decodeFromString(cardString)
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
