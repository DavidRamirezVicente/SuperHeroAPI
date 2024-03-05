package com.example.heroapp.data.remote.responses

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HeroApi {
    @GET("search/{name}")
    suspend fun getHeroInfo(
        @Path("name") heroName: String
    ): Hero
}