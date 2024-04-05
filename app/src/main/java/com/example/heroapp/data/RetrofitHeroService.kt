package com.example.heroapp.data

import com.example.heroapp.data.remote.HeroApi
import com.example.heroapp.data.remote.responses.HeroItemResponse
import com.example.heroapp.domain.HeroServices
import com.example.heroapp.util.Constants

class RetrofitHeroService(private val api: HeroApi) : HeroServices {

    override suspend fun searchHeroByName(name: String): List<HeroItemResponse> {
        val apiKey = Constants.apiKey
        val heroResponse = api.getHeroList(apiKey,name)
        return heroResponse.heroList
    }

    override suspend fun getHeroDetails(heroId: String): HeroItemResponse {
        val apiKey = Constants.apiKey
        return api.getHeroDetails(apiKey,heroId)
    }
}