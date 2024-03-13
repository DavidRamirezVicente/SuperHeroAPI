package com.example.heroapp.data

import com.example.heroapp.data.remote.HeroApi
import com.example.heroapp.data.remote.responses.Hero
import com.example.heroapp.data.remote.responses.HeroResponse
import com.example.heroapp.domain.HeroServices

class RetrofitHeroService(private val api: HeroApi) : HeroServices {

    override suspend fun searchHeroByName(name: String): List<Hero> {
        val heroResponse = api.getHeroList(name)
        return heroResponse.heroList
    }

    override suspend fun getHeroDetails(heroId: String): Hero {
        return api.getHeroDetails(heroId)
    }
}