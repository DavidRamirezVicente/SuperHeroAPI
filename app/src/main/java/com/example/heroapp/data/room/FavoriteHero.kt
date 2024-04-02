package com.example.heroapp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteHero(
    @PrimaryKey
    val id: String,
    val image: String,
    val name: String,
)