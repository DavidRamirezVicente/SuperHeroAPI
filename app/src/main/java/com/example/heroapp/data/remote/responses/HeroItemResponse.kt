package com.example.heroapp.data.remote.responses

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
@Serializable
data class HeroItemResponse(
    val appearance: @Contextual Appearance,
    val biography: @Contextual Biography,
    val connections: @Contextual Connections,
    val id: String,
    val image: @Contextual Image,
    val name: String,
    val powerstats: @Contextual Powerstats,
    val work: @Contextual Work
)