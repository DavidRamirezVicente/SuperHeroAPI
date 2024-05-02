package com.example.heroapp.data.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
class HeroViewModel(
    private val dao: FavoriteHeroDao
): ViewModel() {
//Recibe los eventosd e la UI para realizar las acciones correspondientes
    //private val _state = MutableStateFlow(HeroState())

    /*fun onEvent(event: HeroEvent) {
        when(event) {
            is HeroEvent.DeleteHero -> {
                viewModelScope.launch {
                    dao.deleteHero(event.hero)
                }
            }
            is HeroEvent.AddHero -> {
                viewModelScope.launch {
                    dao.saveHero(event.hero)
                }
            }
        }
    }*/
}
