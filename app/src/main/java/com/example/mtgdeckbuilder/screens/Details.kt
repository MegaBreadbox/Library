package com.example.mtgdeckbuilder.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mtgdeckbuilder.R
import com.example.mtgdeckbuilder.ViewModelProvider
import com.example.mtgdeckbuilder.network.TradingCard
import kotlinx.coroutines.launch

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
        unrestrictedCardNumber = detailsViewModel.unrestrictedCardNumber,
        unrestrictedCardEntry = { detailsViewModel.updateUnrestrictedCardNumber(it)},
        onMenuClick = { detailsViewModel.changeMenuState() },
        maxCardsAllowed = {
            coroutineScope.launch {
                detailsViewModel.maxCardsAllowed(
                    scryfallId = tradingCard.scryfallId
                )
            }
            maxCardsAllowed.value
        },
        isButtonDisabled = { maxCardsAllowed.value != 0},
        onButtonClick = {
             coroutineScope.launch{
                 detailsViewModel.addCardsToDeck(
                    tradingCard = tradingCard,
                    copiesNeeded = detailsViewModel.amountToAdd
                 )
                 detailsViewModel.maxCardsAllowed(tradingCard.scryfallId)
                 detailsViewModel.updateAmountToAdd(detailsViewModel.amountToAdd)

            }
        },
        onMenuItemClick = {
            detailsViewModel.changeMenuState()
            detailsViewModel.updateAmountToAdd(it)
        }
    )
}


@Composable
fun CardDetails(
    tradingCard: TradingCard,
    isMenuEnabled: Boolean,
    amountToAdd: Int,
    unrestrictedCardNumber: String,
    unrestrictedCardEntry: (String) -> Unit,
    onMenuClick: () -> Unit,
    maxCardsAllowed: () -> Int?,
    isButtonDisabled: () -> Boolean,
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
            unrestrictedCardNumber = unrestrictedCardNumber,
            unrestrictedCardEntry = { unrestrictedCardEntry(it) },
            maxCardsAllowed = maxCardsAllowed,
            isButtonDisabled = isButtonDisabled,
            onButtonClick = onButtonClick,
            onMenuItemClick = { onMenuItemClick(it) },
            modifier = modifier.padding(dimensionResource(R.dimen.large_padding))
        )
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
    unrestrictedCardNumber: String,
    unrestrictedCardEntry: (String) -> Unit,
    onMenuClick: () -> Unit,
    onButtonClick: () -> Unit,
    isButtonDisabled: () -> Boolean,
    maxCardsAllowed: () -> Int?,
    onMenuItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
){
    Card(
        modifier = modifier
    ){
        if(maxCardsAllowed()?.let { it <= 0} == true) {
           Text(
               text = stringResource(R.string.reached_card_limit_in_deck),
               color = MaterialTheme.colorScheme.error,
               modifier = modifier
           )
        } else {
            Text(
                text = stringResource(R.string.how_many_would_you_like_to_add_to_the_deck),
                modifier = modifier
            )
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            if(maxCardsAllowed() != null) {
                OutlinedTextField(
                    value = amountToAdd.toString(),
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        CardNumberSelection(
                            isMenuEnabled = isMenuEnabled,
                            onMenuClick = onMenuClick,
                            maxCardsAllowed = maxCardsAllowed,
                            onMenuItemClick = { onMenuItemClick(it) }
                        )
                    },
                    modifier = modifier.width(dimensionResource(R.dimen.number_of_cards_textfield))
                )
            } else {
                OutlinedTextField(
                    value = unrestrictedCardNumber,
                    onValueChange = { unrestrictedCardEntry(it) },
                    supportingText = { Text(stringResource(R.string.enter_a_number)) },
                    modifier = modifier.width(dimensionResource(R.dimen.number_of_cards_textfield)),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
            Button(
                enabled = isButtonDisabled(),
                onClick = { onButtonClick() },
                modifier = modifier
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_to_deck)
                )
                Text(
                    text = stringResource(R.string.card)
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
            contentDescription = stringResource(R.string.quantity_of_cards_to_add),
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