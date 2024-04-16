package com.example.heroapp.repository

import com.example.heroapp.data.remote.HeroApi
import com.example.heroapp.domain.HeroServices
import com.example.heroapp.domain.model.Hero
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class HeroRepository @Inject constructor(
    private val heroService: HeroServices
){
    /*suspend fun getHeroesList(heroName: String): Result<List<HeroItemResponse>> {
        return runCatching {
            val apiKey = Constants.apiKey
            val response = api.getHeroList(heroName, apiKey)
            if (response.response == "error") {
                Timber.d("Error de la API: ${response.response}")
                Result.success(emptyList())
            } else {
                Timber.d("Respuesta de la API recibida correctamente")
                Result.success(response.heroList)
            }
        }.getOrElse { e ->
            Timber.e(e, "Error al obtener la lista de héroes")
            Result.failure(e)
        }
    }

    suspend fun getHeroeInfo(heroId: String): Result<HeroItemResponse> {
        return runCatching {
            val apiKey = Constants.apiKey
            val response = api.getHeroDetails(heroId, apiKey)
            Result.success(response)
        }.getOrElse { e ->
            Result.failure(e)
        }
    }*/


   suspend fun getHeroesList(heroName: String): Result<List<Hero>>{
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
        }.getOrElse { e->
            Result.failure(e)
        }
    }
}
