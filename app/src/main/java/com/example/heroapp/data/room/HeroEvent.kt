package com.example.heroapp.data.room

sealed interface HeroEvent {
    //interfaz para manejar los eventos que un usuario puede hacer en la UI
    data class AddHero(val hero: FavoriteHero): HeroEvent

    data class DeleteHero(val hero: FavoriteHero): HeroEvent

}