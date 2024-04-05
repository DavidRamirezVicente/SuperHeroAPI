package com.example.heroapp.data.remote

import com.example.heroapp.data.remote.responses.HeroItemResponse
import com.example.heroapp.data.remote.responses.HeroResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HeroApi {
    @GET("api/{apiKey}/search/{name}")
    suspend fun getHeroList(
        @Path("name") heroName: String,
        @Path("apiKey") apiKey: String
    ): HeroResponse

    @GET("api/{apiKey}/{heroId}")
    suspend fun getHeroDetails(
        @Path("heroId") heroId: String,
        @Path("apiKey") apiKey: String
    ): HeroItemResponse
}