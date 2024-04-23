package com.example.heroapp.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteHero(
    @PrimaryKey
    val id: String,
    @ColumnInfo
    val image: String,
    @ColumnInfo
    val name: String,
)