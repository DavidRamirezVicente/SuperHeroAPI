package com.example.heroapp.data.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class Powerstats(
    val combat: String,
    val durability: String,
    val intelligence: String,
    val power: String,
    val speed: String,
    val strength: String
)