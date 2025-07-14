package com.example.heroapp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteHeroDao {
    //Metodos para interactuar con la base de datos
    @Query("SELECT * FROM favoritehero")
    fun getFavoriteHeroes(): Flow<List<FavoriteHero>>
    @Insert
    suspend fun saveHero(hero: FavoriteHero)

    @Delete
    suspend fun deleteHero(hero: FavoriteHero)

    @Query("DELETE FROM favoritehero WHERE id = :heroId")
    suspend fun deleteHeroById(heroId: String)

    @Query("SELECT * FROM favoritehero WHERE LOWER(name) LIKE '%' || LOWER(:heroName) || '%'")
    fun searchByName(heroName: String): Flow<List<FavoriteHero>>



}