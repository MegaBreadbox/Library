package com.example.mtgdeckbuilder.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mtgdeckbuilder.R
import com.example.mtgdeckbuilder.ViewModelProvider
import com.example.mtgdeckbuilder.network.CardImage
import com.example.mtgdeckbuilder.network.TradingCard
import com.example.mtgdeckbuilder.ui.theme.MTGDeckBuilderTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun DetailsScreen(
    cardString: String,
    detailsViewModel: DetailsViewModel = viewModel(factory = ViewModelProvider.Factory)
){
    val tradingCard = detailsViewModel.decodeFromString(cardString)
    val maxCardsAllowed = detailsViewModel.maxLegalCards.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    CardDetails(
        tradingCard = tradingCard,
        isMenuEnabled = detailsViewModel.menuState,
        amountToAdd = detailsViewModel.amountToAdd,
        onMenuClick =  { detailsViewModel.changeMenuState() },
        maxCardsAllowed = {
            coroutineScope.launch {
                detailsViewModel.maxCardsAllowed(
                    tradingCardName = tradingCard.name,
                    scryfallId = tradingCard.scryfallId
                )
            }
            maxCardsAllowed.value
        },
        onButtonClick = {
            coroutineScope.launch {
                detailsViewModel.addCardsToDeck(
                    tradingCard = tradingCard,
                    copiesNeeded = detailsViewModel.amountToAdd
                )
                detailsViewModel.updateAmountToAdd(detailsViewModel.amountToAdd)
            }
        },
        onMenuItemClick = { detailsViewModel.updateAmountToAdd(it) }
    )
}


@Composable
fun CardDetails(
    tradingCard: TradingCard,
    isMenuEnabled: Boolean,
    amountToAdd: Int,
    onMenuClick: () -> Unit,
    maxCardsAllowed: () -> Int?,
    onButtonClick: () -> Unit,
    onMenuItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        TradingCardImage(tradingCard = tradingCard)
        AddToDeck(
            isMenuEnabled = isMenuEnabled,
            onMenuClick = onMenuClick,
            amountToAdd = amountToAdd,
            maxCardsAllowed = maxCardsAllowed,
            onButtonClick = onButtonClick,
            onMenuItemClick = { onMenuItemClick(it) },
            modifier = modifier.padding(dimensionResource(R.dimen.large_padding)))
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
    isMenuEnabled: Boolean,
    amountToAdd: Int,
    onMenuClick: () -> Unit,
    onButtonClick: () -> Unit,
    maxCardsAllowed: () -> Int?,
    onMenuItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
){
    Card(
        modifier = modifier
    ){
        Text(
            text ="How many would you like to add to the Deck?",
            modifier = modifier
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedTextField(
                value = amountToAdd.toString(),
                onValueChange = {  },

                trailingIcon = { CardNumberSelection(
                    isMenuEnabled = isMenuEnabled,
                    onMenuClick = onMenuClick,
                    maxCardsAllowed = maxCardsAllowed,
                    onMenuItemClick = { onMenuItemClick(it) }
                ) },
                modifier = modifier.width(dimensionResource(R.dimen.number_of_cards_textfield))
            )
            Button(
                onClick = { onButtonClick() },
                modifier = modifier
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add to deck"
                )
                Text(
                    text = " Card"
                )
            }
        }
    }
}

@Composable
fun CardNumberSelection(
    isMenuEnabled: Boolean,
    onMenuClick: () -> Unit,
    maxCardsAllowed: () -> Int?,
    onMenuItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    Box {
        Icon(
            imageVector = Icons.Rounded.ArrowDropDown,
            contentDescription = "Quantity of cards to add",
            modifier = modifier.clickable { onMenuClick() }
        )
        CardSelectionMenu(
            isMenuEnabled = isMenuEnabled,
            onMenuClick = onMenuClick,
            maxCardsAllowed = maxCardsAllowed,
            onMenuItemClick = { onMenuItemClick(it) }
        )
    }
}

@Composable
fun CardSelectionMenu(
    isMenuEnabled: Boolean,
    onMenuClick: () -> Unit,
    maxCardsAllowed: () -> Int?,
    onMenuItemClick: (Int) -> Unit
) {
    DropdownMenu(
        expanded = isMenuEnabled,
        onDismissRequest = { onMenuClick() }) {
        maxCardsAllowed()?.let {
            repeat(it) { index ->
                DropdownMenuItem(
                    text = { Text(text = (index + 1).toString()) },
                    onClick = { onMenuItemClick(index + 1) }
                )
            }
        }
    }
}