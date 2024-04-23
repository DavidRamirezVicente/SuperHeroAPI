package com.example.heroapp.data.remote.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Biography(
    val aliases: List<String>,
    val alignment: String,
    @SerialName("alter-egos")
    val alterEgos: String? = null,
    @SerialName("first-appearance")
    val firstAppearance: String? = null,
    @SerialName("full-name")
    val fullName: String,
    @SerialName("place-of-birth")
    val placeOfBirth: String? = null,
    val publisher: String
)