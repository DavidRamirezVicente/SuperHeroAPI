package com.example.heroapp.heroList

import android.graphics.Bitmap
import javax.inject.Inject
import android.graphics.drawable.BitmapDrawable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.heroapp.data.remote.responses.models.HeroListEntry
import com.example.heroapp.repository.HeroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber


@HiltViewModel
class HeroListViewModel @Inject constructor(
    private val repository: HeroRepository

): ViewModel() {

    var heroList by mutableStateOf<List<HeroListEntry>>(listOf())

fun loadHeroList(heroName: String){
    Timber.d("Cargando lista de héroes con el nombre: $heroName")
        viewModelScope.launch {
            val result = repository.getHeroesList(heroName)
            result.onSuccess { heroes ->
                heroList = heroes.map {
                    HeroListEntry(
                        heroName = it.name,
                        id = it.id,
                        imageUrl = "https://www.superherodb.com/pictures2/portraits/10/100/${it.id}.jpg"
                    )
                }

            }
            result.onFailure { exception ->
                Timber.tag("HeroListViewModel").e(exception, "Error loading hero list")
            }
        }
    }

    fun calcDominantColor(bitmap : Bitmap, onFinish: (Color) -> Unit) {
        //val bmp = (bitmap as BitmapDrawable). bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}


