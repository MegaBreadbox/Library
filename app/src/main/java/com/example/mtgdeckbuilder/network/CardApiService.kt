package com.example.mtgdeckbuilder.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface CardApiService {
    @GET("cards/search")
    suspend fun getCards(
        @Query("q") q : String
    ): CardList

    @GET
    suspend fun nextPage(
        @Url url: String?
    ): CardList

}