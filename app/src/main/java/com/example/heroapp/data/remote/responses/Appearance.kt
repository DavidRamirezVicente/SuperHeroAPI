package com.example.heroapp.data.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class Appearance(
    val eyeColor: String? = null,
    val gender: String,
    val hairColor: String? = null,
    val height: List<String>,
    val race: String,
    val weight: List<String>

)