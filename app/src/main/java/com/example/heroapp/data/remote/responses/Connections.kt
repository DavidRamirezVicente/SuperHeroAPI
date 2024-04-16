package com.example.heroapp.data.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class Connections(
    val groupAffiliation: String? = null,
    val relatives: String
)