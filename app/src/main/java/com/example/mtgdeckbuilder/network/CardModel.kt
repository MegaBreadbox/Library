package com.example.mtgdeckbuilder.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardList (
    @SerialName(value = "has_more")
    val hasMore: Boolean,
    @SerialName(value = "next_page")
    val nextPage: String?,
    val data: Array<TradingCard>

)

@Serializable
data class TradingCard (
    @SerialName(value = "id")
    val scryfallId: String,
    val name: String,
    @SerialName(value = "image_uris")
    val imageUris: CardImage?,
    val cmc: Float?,
    @SerialName(value = "color_identity")
    val colorIndentity: Array<String>

)

@Serializable
data class CardImage (
    val normal: String,
    val large: String,
    val png: String
)