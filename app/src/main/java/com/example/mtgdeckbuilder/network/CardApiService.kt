package com.example.mtgdeckbuilder.network

import retrofit2.http.GET
import retrofit2.http.Query

interface CardApiService {
    @GET("cards/search")
    suspend fun getCards(
        @Query("q") q : String
    ): CardList
}