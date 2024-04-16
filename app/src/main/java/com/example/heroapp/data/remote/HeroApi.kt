package com.example.heroapp.data.remote

import com.example.heroapp.data.remote.responses.HeroItemResponse
import com.example.heroapp.data.remote.responses.HeroResponse
import com.example.heroapp.domain.mapper.HeroMapper
import com.example.heroapp.domain.model.Hero
import retrofit2.http.GET
import retrofit2.http.Path

interface HeroApi {
    @GET("api/{apiKey}/search/{name}")
    suspend fun getHeroList(
        @Path("apiKey") apiKey: String,
        @Path("name") heroName: String

    ): HeroResponse

    @GET("api/{apiKey}/{heroId}")
    suspend fun getHeroDetails(
        @Path("apiKey") apiKey: String,
        @Path("heroId") heroId: String
    ): HeroItemResponse
}