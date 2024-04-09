package com.example.heroapp.domain.model

import com.example.heroapp.data.remote.responses.Appearance
import com.example.heroapp.data.remote.responses.Biography
import com.example.heroapp.data.remote.responses.Connections
import com.example.heroapp.data.remote.responses.Powerstats
import com.example.heroapp.data.remote.responses.Work

data class Hero(
    val appearance: Appearance,
    val biography: Biography,
    val id: String,
    val image: String,
    val name: String,
    val powerstats: Powerstats,
)