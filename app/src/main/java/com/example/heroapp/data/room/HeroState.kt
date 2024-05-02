package com.example.heroapp.data.room

data class HeroState(
    //Clase que contendra los heroes que estan actualmente visibles
    val favHeroes: List<FavoriteHero> = emptyList()
) {


}