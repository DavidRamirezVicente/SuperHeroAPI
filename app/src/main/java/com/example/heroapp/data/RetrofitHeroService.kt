package com.example.heroapp.data

import com.example.heroapp.data.remote.HeroApi
import com.example.heroapp.data.remote.responses.HeroItemResponse
import com.example.heroapp.domain.HeroServices
import com.example.heroapp.domain.mapper.HeroMapper
import com.example.heroapp.domain.model.Hero
import com.example.heroapp.util.Constants

class RetrofitHeroService(private val api: HeroApi) : HeroServices {

    override suspend fun searchHeroByName(name: String): List<Hero> {

        val apiKey = Constants.apiKey
        val heroResponse = api.getHeroList(apiKey,name)
        return heroResponse.heroList.map { HeroMapper.buildFrom(response = it) }
    }

    override suspend fun getHeroDetails(heroId: String): Hero {
        val apiKey = Constants.apiKey
        val heroResponse = api.getHeroDetails(apiKey, heroId)
        return HeroMapper.buildFrom(heroResponse)
    }
}