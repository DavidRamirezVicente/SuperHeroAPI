package com.example.heroapp.data.room

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import com.example.heroapp.repository.HeroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FavoriteHeroViewModel @Inject constructor(
    val repository: HeroRepository
): ViewModel() {
    val allFavoriteHeroes: Flow<List<FavoriteHero>> = repository.allFavorites

    fun calcDominantColor(bitmap: Bitmap, onFinish: (Color) -> Unit) {
        val compatibleBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(compatibleBitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }



}
