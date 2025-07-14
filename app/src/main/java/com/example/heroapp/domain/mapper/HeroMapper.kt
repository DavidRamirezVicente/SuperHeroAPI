package com.example.heroapp.domain.mapper

import com.example.heroapp.data.remote.responses.HeroItemResponse
import com.example.heroapp.data.remote.responses.PowerstatsResponse
import com.example.heroapp.data.remote.responses.StatsResponse
import com.example.heroapp.domain.model.Hero
import com.example.heroapp.domain.model.PowerStats

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

    fun mapPowerStats(response: StatsResponse):PowerStats {
        return PowerStats(
            combat =  response.combat,
            durability = response.durability,
            intelligence = response.intelligence,
            power = response.power,
            speed = response.speed,
            strength = response.strength
        )
    }
}