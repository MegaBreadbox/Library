package com.example.mtgdeckbuilder.data

import com.example.mtgdeckbuilder.network.CardApiService
import com.example.mtgdeckbuilder.network.CardList

interface CardListRepository {
    suspend fun getCardList(input: String): CardList
}

class NetworkCardListRepository(
    private val cardApiService: CardApiService
): CardListRepository {
    override suspend fun getCardList(input: String): CardList = cardApiService.getCards(input)
}