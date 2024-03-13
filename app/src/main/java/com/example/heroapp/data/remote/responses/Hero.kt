package com.example.heroapp.data.remote.responses

data class Hero(
    val appearance: Appearance,
    val biography: Biography,
    val connections: Connections,
    val id: Int,
    val image: Image,
    val name: String,
    val powerstats: Powerstats,
    val work: Work
)