package com.example.heroapp.heroList

import android.graphics.Bitmap
import android.graphics.BitmapShader
import javax.inject.Inject
import android.graphics.drawable.BitmapDrawable

import android.graphics.drawable.Drawable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.heroapp.data.RetrofitHeroService
import com.example.heroapp.data.remote.responses.Hero
import com.example.heroapp.data.remote.responses.HeroResponse
import com.example.heroapp.data.remote.responses.models.HeroListEntry
import com.example.heroapp.repository.HeroRepository
import com.example.heroapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.xml.transform.Result


@HiltViewModel
class HeroListViewModel @Inject constructor(
    private val repository: HeroRepository

): ViewModel() {


    var heroList by mutableStateOf<List<HeroListEntry>>(listOf())

/*fun loadHeroList(heroName: String){
        viewModelScope.launch {
            val result = repository.getHeroesList(heroName)
            when (result) {
                is Resource.Success<List<Hero>> -> {
                    heroList = result.data.map { hero ->
                        HeroListEntry(hero.name, hero.image.url)
                    }
                }
                is Resource.Error -> {
                    heroList = emptyList()
                }
            }
        }
    }*/


    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}

