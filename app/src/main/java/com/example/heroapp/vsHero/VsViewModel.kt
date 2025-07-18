package com.example.heroapp.vsHero

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroapp.R
import com.example.heroapp.data.room.FavoriteHero
import com.example.heroapp.domain.VSActions
import com.example.heroapp.domain.VSStates
import com.example.heroapp.repository.HeroRepository
import com.example.heroapp.repository.VSRepository
import com.example.heroapp.util.StateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VsViewModel @Inject constructor(
    private val vsRepository: VSRepository,
    private val heroRepository: HeroRepository,
    private val stateMachine: StateMachine
) : ViewModel() {


    val state = MutableStateFlow<VSStates>(VSStates.NoActiveMatch)
    val _isRollEnabled = MutableStateFlow(true)
    val isRollEnabled: StateFlow<Boolean> get() = _isRollEnabled

    init {
        viewModelScope.launch {
            stateMachine.state.collect { newState ->
                state.value = newState
            }
        }
    }

    val allFavoriteHeroes: Flow<List<FavoriteHero>> = heroRepository.allFavorites

    private val _diceResult = MutableStateFlow(0)
    val diceResult: StateFlow<Int> get() = _diceResult

    fun startMatch() {
        viewModelScope.launch {
            dispatch(VSActions.Begin)
        }
    }

    fun onCellSelect(slotId: Int?) {
        viewModelScope.launch {
            dispatch(VSActions.SearchParticipant(slotId))
        }
    }


    fun selectParticipant(hero: FavoriteHero, slotId: Int?) {
        viewModelScope.launch {
            dispatch(VSActions.SelectParticipant(hero, slotId))
        }
    }

    fun startFight() {
        viewModelScope.launch {
            dispatch(VSActions.StartBattle)
        }
    }
    fun getStatIcon(stat: String): Int {
        return when (stat) {
            "Combat" -> R.drawable.fist_removebg_preview
            "Durability" -> R.drawable.logo_shield_png
            "Intelligence" -> R.drawable.intelligence_removebg_preview
            "Power" -> R.drawable.power_removebg_preview
            "Speed" -> R.drawable.speed_removebg_preview
            "Strength" -> R.drawable.strength_removebg_preview
            else -> R.drawable.dice_1
        }
    }

    fun rollDice() {
        if (_isRollEnabled.value.not()) return
        _diceResult.value = (1..6).random()
        _isRollEnabled.value = false
        viewModelScope.launch {
            val currentState = state.value
            if (currentState is VSStates.RollingDice) {
                val category = getPowerStat(diceResult.value)
                val firstHeroStat = loadPowerStatValue(currentState.firstContestant.id, category) ?: 0
                val secondHeroStat = loadPowerStatValue(currentState.secondContestant.id, category) ?: 0
                val statIcon = getStatIcon(category)
                dispatch(VSActions.SetRoundResult(statIcon, firstHeroStat, secondHeroStat))
                _isRollEnabled.value = true
            }
        }
    }

    fun returnToStart() {
        dispatch(VSActions.Return)
    }

    fun getPowerStat(result: Int): String {
        return when (result) {
            1 -> "Combat"
            2 -> "Durability"
            3 -> "Intelligence"
            4 -> "Power"
            5 -> "Speed"
            6 -> "Strength"
            else -> ""
        }
    }

    fun imageResource(result: Int): Int {
        return when (result) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
    }

    private suspend fun loadPowerStatValue(heroId: String, category: String): Int? {
        return withContext(Dispatchers.IO) {
            var powerStatValue: Int? = null
            val result = vsRepository.getHeroPowerStats(heroId)
            result.onSuccess { powerstats ->
                powerStatValue = when (category) {
                    "Combat" -> powerstats.combat.toIntOrNull()
                    "Durability" -> powerstats.durability.toIntOrNull()
                    "Intelligence" -> powerstats.intelligence.toIntOrNull()
                    "Power" -> powerstats.power.toIntOrNull()
                    "Speed" -> powerstats.speed.toIntOrNull()
                    "Strength" -> powerstats.strength.toIntOrNull()
                    else -> 0
                }
            }.onFailure { exception ->
                Timber.tag("Powerstats").e(exception, "Error loading powerstats")
            }
            powerStatValue
        }
    }

    /*private suspend fun calculateScores() {
        Timber.d("Calculate Scores")
        val currentState = state.value
        if (currentState is VSStates.RollingDice) {
            val category = getPowerStat(diceResult.value)

            val imageResource = when (diceResult.value) {
                1 -> R.drawable.fist_removebg_preview
                2 -> R.drawable.yunke
                3 -> R.drawable.intelligence_removebg_preview
                4 -> R.drawable.power_removebg_preview
                5 -> R.drawable.speed_removebg_preview
                else -> R.drawable.strength_removebg_preview
            }

            val firstHeroStatValue =  loadPowerStatValue(currentState.firstContestant.id, category) ?: 0
            val secondHeroStatValue =  loadPowerStatValue(currentState.secondContestant.id, category) ?: 0

            if (firstHeroStatValue > secondHeroStatValue) {
                winsFirst++
                Timber.d("Heroe1 gana")
            } else if (firstHeroStatValue < secondHeroStatValue) {
                winsSecond++
                Timber.d("Heroe2 gana")
            } else {
                winsFirst++
                winsSecond++
                Timber.d("Empate")
            }

            val roundResult = VSStates.RoundResult(
                category = imageResource,
                statValue1 = firstHeroStatValue,
                statValue2 = secondHeroStatValue
            )
            Timber.d("Resultados: $roundResult")
            roundResults.add(roundResult)

            val battlingState = VSStates.Battling(
                firstContestant = currentState.firstContestant,
                secondContestant =  currentState.secondContestant,
                winsFirstContestant = winsFirst,
                winsSecondContestant = winsSecond,
                rounds = roundResults.toList()
            )
            dispatch(
                VSActions.SetRoundResult(
                    imageResource,
                    firstHeroStatValue,
                    secondHeroStatValue
                )
            )
            state.value = battlingState
        }

    }
    private suspend fun winner(hero: FavoriteHero){
        val currenState = state.value
        val winner = VSStates.Winner(hero)
        if (currenState is VSStates.Battling){
            if (winsFirst == 3){
                winner
            }
            if (winsSecond == 3){
                winner
            }
            if (winsSecond == 3 && winsFirst==3){
                Timber.d("Empate")

            }
        }


    }*/

    fun dispatch(action: VSActions) {
        viewModelScope.launch {
            stateMachine.dispatch(action)
        }
    }
}




