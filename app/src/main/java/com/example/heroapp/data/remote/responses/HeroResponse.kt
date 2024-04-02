package com.example.heroapp.data.remote.responses

import com.google.gson.annotations.SerializedName

data class HeroResponse(
    val response: String,
    @SerializedName("results")
    val heroList: List<Hero>,
    val resultsFor: String
)