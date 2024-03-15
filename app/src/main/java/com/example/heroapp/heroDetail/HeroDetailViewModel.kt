package com.example.heroapp.heroDetail

import androidx.lifecycle.ViewModel
import com.example.heroapp.data.remote.responses.Hero
import com.example.heroapp.repository.HeroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel @Inject constructor(
    private val repository: HeroRepository
): ViewModel() {

    suspend fun getHeroInfo(id: String): Result<Hero> {
        return  repository.getHeroeInfo(id)
    }
}