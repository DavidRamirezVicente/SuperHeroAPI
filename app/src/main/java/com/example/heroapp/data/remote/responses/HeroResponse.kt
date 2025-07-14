package com.example.heroapp.data.remote.responses

import android.annotation.SuppressLint
import com.example.heroapp.domain.mapper.HeroMapper
import com.example.heroapp.domain.model.Hero
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class HeroResponse(
    val response: String,
    @SerialName("results")
    val heroList: List<HeroItemResponse>,
    @SerialName("results-for")
    val resultsFor: String
)