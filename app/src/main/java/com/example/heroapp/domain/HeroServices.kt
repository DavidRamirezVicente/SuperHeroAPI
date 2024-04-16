package com.example.heroapp.domain

import com.example.heroapp.data.remote.responses.HeroItemResponse
import com.example.heroapp.domain.mapper.HeroMapper
import com.example.heroapp.domain.model.Hero

interface HeroServices {
    // Llamada de red
    // Mapeo de response type a dominio
    suspend fun searchHeroByName(name: String): List<Hero>
    suspend fun getHeroDetails(heroId: String): Hero
}