package com.example.heroapp.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class PowerStats(
    val combat: String,
    val durability: String,
    val intelligence: String,
    val power: String,
    val speed: String,
    val strength: String
)