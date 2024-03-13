package com.example.heroapp.domain

import com.example.heroapp.data.remote.responses.Hero

interface HeroServices {
    suspend fun searchHeroByName(name: String): List<Hero>
    suspend fun getHeroDetails(heroId: String): Hero
}