package com.example.heroapp.data.remote.responses

data class Hero(
    val response: String,
    val results: List<Result>,
    val resultsFor: String
)