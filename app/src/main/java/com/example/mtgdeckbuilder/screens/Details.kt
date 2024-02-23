package com.example.mtgdeckbuilder.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mtgdeckbuilder.R
import com.example.mtgdeckbuilder.network.CardImage
import com.example.mtgdeckbuilder.network.TradingCard
import com.example.mtgdeckbuilder.ui.theme.MTGDeckBuilderTheme
import kotlinx.serialization.json.Json

//TO DO: find a cleaner way to pass card object than reusing search viewModel
@Composable
fun detailsScreen(
    cardString: String
){
    cardDetails(
        tradingCard = Json.decodeFromString(cardString)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun cardDetails(
    tradingCard: TradingCard,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TradingCardImage(tradingCard = tradingCard)
        AddToDeck()
    }
}
@Composable
fun TradingCardImage(
    tradingCard: TradingCard
) {
    Card(
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.medium_padding))
    ){
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(tradingCard.imageUris?.png)
                .build(),
            contentDescription = null,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToDeck(
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.padding(dimensionResource(R.dimen.large_padding))
    ){
        Text("How many would you like to add to the Deck?")
        Row() {
            OutlinedTextField(
                value = "0",
                onValueChange = { },
                readOnly = true,
                trailingIcon = { CardNumberSelection() },
                modifier = modifier.width(dimensionResource(R.dimen.number_of_cards_textfield))
            )
            Spacer(modifier.weight(1F))
            Button(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add to deck"
                )
            }
        }

    }
}

@Composable
fun CardNumberSelection(
    modifier: Modifier = Modifier
){
    Icon(
        imageVector = Icons.Rounded.ArrowDropDown,
        contentDescription = "Quantity of cards to add",
        modifier = modifier.clickable{}
    )
    CardSelectionMenu()
}

@Composable
fun CardSelectionMenu() {
    DropdownMenu(
        expanded = true,
        onDismissRequest = { /*TODO*/ }) {
        DropdownMenuItem(text ={ Text("Hello") }, onClick = { /*TODO*/ })
        DropdownMenuItem(text ={ Text("Hello") }, onClick = { /*TODO*/ })
        DropdownMenuItem(text ={ Text("Hello") }, onClick = { /*TODO*/ })
    }
}