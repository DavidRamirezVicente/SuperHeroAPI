package com.example.heroapp.data.remote

import com.example.heroapp.data.remote.responses.Hero
import com.example.heroapp.data.remote.responses.HeroResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HeroApi {
    @GET("api/{apiKey}/search/{name}")
    suspend fun getHeroList(
        @Path("name") heroName: String,
        @Path("apiKey") apiKey: String
    ): HeroResponse

    @GET("hero/{character_id}")
    suspend fun getHeroDetails(
        @Path("character_id") heroId: String
    ): Hero
}