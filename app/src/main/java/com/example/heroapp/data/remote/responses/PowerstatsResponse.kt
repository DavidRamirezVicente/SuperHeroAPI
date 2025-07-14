package com.example.heroapp.data.remote.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PowerstatsResponse(
    val combat: String,
    val durability: String,
    val intelligence: String,
    val power: String,
    val speed: String,
    val strength: String
)