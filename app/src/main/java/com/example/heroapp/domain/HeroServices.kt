package com.example.heroapp.domain

import com.example.heroapp.data.remote.responses.HeroItemResponse

interface HeroServices {
    // Llamada de red
    // Mapeo de response type a dominio
    suspend fun searchHeroByName(name: String): List<HeroItemResponse>
    suspend fun getHeroDetails(heroId: String): HeroItemResponse
}