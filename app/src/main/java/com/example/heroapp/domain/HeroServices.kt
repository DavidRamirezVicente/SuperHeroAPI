package com.example.heroapp.domain

import com.example.heroapp.data.remote.responses.PowerstatsResponse
import com.example.heroapp.domain.model.Hero
import com.example.heroapp.domain.model.PowerStats

interface HeroServices {

    suspend fun searchHeroByName(name: String): List<Hero>
    suspend fun getHeroDetails(heroId: String): Hero
    suspend fun getHeroPowerStats(heroId: String): PowerStats

}