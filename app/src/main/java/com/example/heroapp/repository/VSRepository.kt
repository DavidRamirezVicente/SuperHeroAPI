package com.example.heroapp.repository

import com.example.heroapp.data.room.FavoriteHero
import com.example.heroapp.data.room.FavoriteHeroDatabase
import com.example.heroapp.domain.HeroServices
import com.example.heroapp.domain.model.PowerStats
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityScoped
class VSRepository @Inject constructor(
    private val heroService: HeroServices,
    private val db: FavoriteHeroDatabase
) {
    fun searchHeroes(name: String): Flow<List<FavoriteHero>> {
        return db.heroDao.searchByName(name)
        }
    suspend fun getHeroPowerStats(heroId: String): Result<PowerStats>{
        return runCatching {
            val powerStats = heroService.getHeroPowerStats(heroId)
            Result.success(powerStats)
        }.getOrElse { e ->
            Result.failure(e)
        }
    }
}