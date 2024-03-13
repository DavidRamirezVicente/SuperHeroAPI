package com.example.heroapp.data.remote.responses

data class HeroResponse(
    val response: String,
    val heroList: List<Hero>,
    val resultsFor: String
)