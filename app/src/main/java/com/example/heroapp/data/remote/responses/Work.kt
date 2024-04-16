package com.example.heroapp.data.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class Work(
    val base: String,
    val occupation: String
)