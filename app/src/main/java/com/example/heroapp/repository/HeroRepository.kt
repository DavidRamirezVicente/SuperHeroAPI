package com.example.heroapp.repository

import com.example.heroapp.data.remote.HeroApi
import com.example.heroapp.data.remote.responses.HeroItemResponse
import com.example.heroapp.util.Constants
import dagger.hilt.android.scopes.ActivityScoped
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class HeroRepository @Inject constructor(
    private val api: HeroApi
){
    suspend fun getHeroesList(heroName: String): Result<List<HeroItemResponse>> {
        return runCatching {
            val apiKey = Constants.apiKey;
            val response = api.getHeroList(heroName,apiKey)
            Timber.d("Respuesta de la API recibida correctamente")
            Result.success(response.heroList)
        }.getOrElse { e ->
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
    }
}
