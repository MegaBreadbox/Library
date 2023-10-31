package com.example.mtgdeckbuilder.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Card (
    @SerialName(value = "has_more")
    val hasMore: Boolean,
    @SerialName(value = "next_page")
    val nextPage: String,

)