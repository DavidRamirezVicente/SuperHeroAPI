package com.example.heroapp.data.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class StatsResponse(
    val combat: String,
    val durability: String,
    val id: String,
    val intelligence: String,
    val name: String,
    val power: String,
    val response: String,
    val speed: String,
    val strength: String
)