package com.example.heroapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.heroapp.data.remote.responses.Hero

@Dao
interface FavoriteHeroDao {
    @Insert
    suspend fun insertHero(hero: FavoriteHero)

    @Delete
    suspend fun deleteHero(hero: FavoriteHero)

    @Query("SELECT * FROM favoritehero")
    fun getFavoriteHeroes(): LiveData<List<FavoriteHero>>
}