package com.example.heroapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteHero::class],
    version = 1
)
abstract class FavoriteHeroDatabase: RoomDatabase() {

    abstract val heroDao: FavoriteHeroDao
}