package com.example.heroapp.data.remote.responses

import android.annotation.SuppressLint
import com.example.heroapp.domain.model.PowerStats
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class HeroItemResponse(
    val appearance: @Contextual Appearance,
    val biography: @Contextual Biography,
    val connections: @Contextual Connections,
    val id: String,
    val image: @Contextual Image,
    val name: String,
    val powerstats: @Contextual PowerStats,
    val work: @Contextual Work
)