package com.example.heroapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.heroapp.data.remote.responses.Hero
@Database(
    entities = [FavoriteHero::class],
    version = 1
)
abstract class FavoriteHeroDatabase: RoomDatabase() {

    abstract val dao: FavoriteHeroDao
}