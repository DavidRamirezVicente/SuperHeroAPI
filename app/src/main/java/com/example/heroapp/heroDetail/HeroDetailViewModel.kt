package com.example.heroapp.heroDetail

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.heroapp.data.room.FavoriteHero
import com.example.heroapp.data.room.FavoriteHeroDao
import com.example.heroapp.data.room.HeroEvent
import com.example.heroapp.domain.model.Hero
import com.example.heroapp.repository.HeroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: HeroRepository,
): ViewModel() {

    val heroId: String = savedStateHandle.get<String>("heroId") ?: ""
    suspend fun getHeroInfo(id: String): Result<Hero> {
        return repository.getHeroeInfo(id)
    }

    val isFavorite: StateFlow<Boolean> = repository.allFavorites.map { hero ->
        hero.any{
            it.id == heroId
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

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

