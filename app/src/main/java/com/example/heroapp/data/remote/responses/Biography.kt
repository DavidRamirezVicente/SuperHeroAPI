package com.example.heroapp.data.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class Biography(
    val aliases: List<String>,
    val alignment: String,
    val alterEgos: String? = null,
    val firstAppearance: String? = null,
    val fullName: String? = null,
    val placeOfBirth: String? = null,
    val publisher: String
)