package com.example.heroapp.repository

import com.example.heroapp.data.remote.HeroApi
import com.example.heroapp.data.remote.responses.Hero
import com.example.heroapp.data.remote.responses.HeroResponse
import com.example.heroapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class HeroRepository @Inject constructor(
    private val api: HeroApi
){
    suspend fun getHeroesList(heroName: String): Result<List<Hero>> {
        return runCatching {
            val response = api.getHeroList(heroName)
            Result.success(response.heroList)
        }.getOrElse { e ->
            Result.failure(e)
        }
    }

    suspend fun getHeroeInfo(heroId: String): Result<Hero> {
        return runCatching {
            val response = api.getHeroDetails(heroId)
            Result.success(response)
        }.getOrElse { e ->
            Result.failure(e)
        }
    }
}
