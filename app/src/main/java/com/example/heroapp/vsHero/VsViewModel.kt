package com.example.heroapp.vsHero

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.heroapp.data.remote.responses.PowerstatsResponse
import com.example.heroapp.data.remote.responses.models.HeroVsBaseStats
import com.example.heroapp.data.room.FavoriteHero
import com.example.heroapp.domain.VSActions
import com.example.heroapp.domain.VSStates
import com.example.heroapp.domain.model.PowerStats
import com.example.heroapp.repository.HeroRepository
import com.example.heroapp.repository.VSRepository
import com.example.heroapp.util.StateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class VsViewModel @Inject constructor(
    private val vsRepository: VSRepository,
    private val heroRepository: HeroRepository,
    private val stateMachine: StateMachine
): ViewModel() {


    val allFavoriteHeroes: Flow<List<FavoriteHero>> = heroRepository.allFavorites

    private val _state = MutableStateFlow<VSStates>(VSStates.NoActiveMatch)
    val state: StateFlow<VSStates> get() = _state

    private val _slotId = MutableStateFlow(1)
    val slotId: StateFlow<Int> get() = _slotId

    private val _diceResult = MutableStateFlow(0)
    val diceResult: StateFlow<Int> get() = _diceResult

    private val _hero1Score = MutableStateFlow(0)
    val hero1Score: StateFlow<Int> get() = _hero1Score

    private val _hero2Score = MutableStateFlow(0)
    val hero2Score: StateFlow<Int> get() = _hero2Score

    private val _firstHeroStatValue = MutableStateFlow(0)
    val firstHeroStatValue: StateFlow<Int> get() = _firstHeroStatValue

    private val _secondHeroStatValue = MutableStateFlow(0)
    val secondHeroStatValue: StateFlow<Int> get() = _secondHeroStatValue

    private val _heroVsBasesStatsList = mutableStateListOf<HeroVsBaseStats>()

    val heroVsBasesStatsList: SnapshotStateList<HeroVsBaseStats> = _heroVsBasesStatsList

    var score1: Int
        get() = _hero1Score.value
        set(value){
            _hero1Score.value = value
        }
    var score2: Int
        get() = _hero2Score.value
        set(value){
            _hero2Score.value = value
        }


    init {
        observeStateMachine()
    }

    private fun observeStateMachine() {
        viewModelScope.launch {
            stateMachine.state.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun startMatch() {
        viewModelScope.launch {
            stateMachine.dispatch(VSActions.Begin)
        }
    }

    fun onCellSelect(slotId: Int) {
        viewModelScope.launch {
            stateMachine.dispatch(VSActions.SearchParticipant(slotId))
        }
    }

    fun searchParticipants(query: String) {
        viewModelScope.launch {
            val heroesFlow = vsRepository.searchHeroes(query)
            stateMachine.dispatch(VSActions.UpdatedSearch(query, heroesFlow))
        }
    }

    fun selectParticipant(hero: FavoriteHero, slotId: Int) {
        viewModelScope.launch {
            stateMachine.dispatch(VSActions.SelectParticipant(hero, slotId))
        }
    }

    fun startFight() {
        viewModelScope.launch {
            stateMachine.dispatch(VSActions.StartBattle)
        }
    }

    fun rollDice() {
        _diceResult.value = (1..6).random()
        viewModelScope.launch {
            stateMachine.dispatch(VSActions.PickRandomCategory)
        }
    }

    suspend fun loadPowerStatValue(heroId: String): Int? {
        Timber.d("Cargando estadisticas")
        return withContext(Dispatchers.IO) {
            var powerStatValue: Int? = null
            val result = vsRepository.getHeroPowerStats(heroId)
            result.onSuccess { powerstats ->
                powerStatValue = when (_diceResult.value) {
                    1 -> powerstats.combat.toIntOrNull()
                    2 -> powerstats.durability.toIntOrNull()
                    3 -> powerstats.intelligence.toIntOrNull()
                    4 -> powerstats.power.toIntOrNull()
                    5 -> powerstats.speed.toIntOrNull()
                    else -> powerstats.strength.toIntOrNull()
                }
            }.onFailure { exception ->
                Timber.tag("Powerstats").e(exception, "Error loading powerstats")
            }
            powerStatValue
        }
    }

    fun calculateScores(firstHero: FavoriteHero, secondHero: FavoriteHero) {
        viewModelScope.launch {
            val firstHeroStatValue = loadPowerStatValue(firstHero.id) ?: 0
            val secondHeroStatValue = loadPowerStatValue(secondHero.id) ?: 0

            _firstHeroStatValue.value = firstHeroStatValue
            _secondHeroStatValue.value = secondHeroStatValue

            Timber.d("Hero1: ${_firstHeroStatValue.value}")
            Timber.d("Hero2: ${_secondHeroStatValue.value}")

            when {
                firstHeroStatValue > secondHeroStatValue -> {
                    score1 += 1
                    _hero1Score.value = score1
                    Timber.d("Hero1 ha ganado")
                }
                firstHeroStatValue < secondHeroStatValue -> {
                    score2 += 1
                    _hero2Score.value = score2
                    Timber.d("Heroe2 ha ganado")
                }
                else -> {
                    score1 += 1
                    score2 += 1
                    _hero1Score.value = score1
                    _hero2Score.value = score2

                    Timber.d("Empate")
                }
            }
        }
    }
    fun addHeroVsBaseStats(heroVsBaseStats: HeroVsBaseStats) {
        _heroVsBasesStatsList.add(heroVsBaseStats)
    }
}


