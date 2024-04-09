package com.example.heroapp.domain.mapper

import com.example.heroapp.data.remote.responses.HeroItemResponse
import com.example.heroapp.domain.model.Hero

object HeroMapper {
    fun buildFrom(response: HeroItemResponse ): Hero {
        return Hero(
            appearance = response.appearance,
            biography = response.biography,
            id = response.id,
            image = response.image.url,
            name = response.name,
            powerstats = response.powerstats
        )
    }
}