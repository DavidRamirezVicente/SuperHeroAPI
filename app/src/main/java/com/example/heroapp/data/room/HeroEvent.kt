package com.example.heroapp.data.room

import com.example.heroapp.data.remote.responses.HeroItemResponse

sealed interface HeroEvent {
    object SaveHero: HeroEvent
    //¿Poner los campos de Hero?

    data class DeleteHero(val hero: HeroItemResponse): HeroEvent

}