package com.example.heroapp.repository

import com.example.heroapp.data.room.FavoriteHero
import com.example.heroapp.data.room.FavoriteHeroDatabase
import com.example.heroapp.domain.HeroServices
import com.example.heroapp.domain.model.Hero
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityScoped
class HeroRepository @Inject constructor(
    private val heroService: HeroServices,
    private val dataBase: FavoriteHeroDatabase,
) {
    // implementar DAO
    suspend fun getHeroesList(heroName: String): Result<List<Hero>> {
        return runCatching {
            val heroes = heroService.searchHeroByName(heroName)
            Result.success(heroes)
        }.getOrElse { e ->
            Result.failure(e)
        }
    }

    suspend fun getHeroeInfo(heroId: String): Result<Hero> {
        return runCatching {
            val hero = heroService.getHeroDetails(heroId)
            Result.success(hero)
        }.getOrElse { e ->
            Result.failure(e)
        }
    }

    val allFavorites: Flow<List<FavoriteHero>> = dataBase.heroDao.getFavoriteHeroes()

    suspend fun delete(heroId: String) {
        dataBase.heroDao.deleteHeroById(heroId)
    }

    suspend fun save(hero: FavoriteHero){
        dataBase.heroDao.saveHero(hero)
    }

}
