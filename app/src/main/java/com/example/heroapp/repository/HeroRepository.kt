package com.example.heroapp.repository

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
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
    }
}
