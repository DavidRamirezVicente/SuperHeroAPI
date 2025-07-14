package com.example.heroapp.domain

import com.example.heroapp.data.room.FavoriteHero
import kotlinx.coroutines.flow.Flow

sealed class VSStates {
    data object NoActiveMatch : VSStates()
    data class SettingUpMatch(
        val firstContestant: FavoriteHero?,
        val secondContestant: FavoriteHero?
    ) : VSStates()
    data class SettingUpSearch(
        val previousState: SettingUpMatch,
        val queryText: String,
        val candidatesToPick: Flow<List<FavoriteHero?>>,
        val slotId: Int?
    ) : VSStates()
    data class SetupComplete(
        val firstContestant: FavoriteHero,
        val secondContestant: FavoriteHero,
    ) : VSStates()

    data class Battling(
        val firstContestant: FavoriteHero,
        val secondContestant: FavoriteHero,
        val winsFirstContestant: Int,
        val winsSecondContestant: Int,
        val rounds: List<RoundResult>
    ) : VSStates()

    data class RoundResult(
        val category: Int,
        val statValue1: Int,
        val statValue2: Int
    )
    data class RollingDice(
        val previousState: Battling,
        val firstContestant: FavoriteHero,
        val secondContestant: FavoriteHero,
    ) : VSStates()

    data class Winner(
        val hero: FavoriteHero?
    ): VSStates()

}


/*data class CategoryPick(
     val category: PowerStats
 ) : VSStates()*/