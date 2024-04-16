package com.example.heroapp.data.remote.responses.models

import com.google.gson.annotations.SerializedName


data class HeroListEntry(
    val heroName: String,
    val id: String,
    val imageUrl: String
)
