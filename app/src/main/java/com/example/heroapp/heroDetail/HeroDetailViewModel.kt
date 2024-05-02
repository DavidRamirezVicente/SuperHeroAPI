package com.example.heroapp.heroDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroapp.data.room.FavoriteHero
import com.example.heroapp.data.room.FavoriteHeroDao
import com.example.heroapp.data.room.HeroEvent
import com.example.heroapp.domain.model.Hero
import com.example.heroapp.repository.HeroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel @Inject constructor(
    private val repository: HeroRepository,
): ViewModel() {

    suspend fun getHeroInfo(id: String): Result<Hero> {
        return repository.getHeroeInfo(id)
    }


    val isFavorite: StateFlow<Boolean>
    fun saveFavoriteHero(id: String, name: String, imageUrl: String) {
        viewModelScope.launch {
            val hero = FavoriteHero(id,name,imageUrl)
            repository.save(hero)

        }
    }
    fun deleteFavoriteHero(heroId: String) {
        viewModelScope.launch {
            repository.delete(heroId)
        }
    }
}

