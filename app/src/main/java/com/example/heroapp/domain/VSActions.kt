package com.example.heroapp.domain

import com.example.heroapp.data.room.FavoriteHero
import kotlinx.coroutines.flow.Flow

sealed interface VSActions {
    data object Begin : VSActions
    data class SearchParticipant(val slotId: Int) : VSActions
    data class SelectParticipant(val hero: FavoriteHero, val slotId: Int) : VSActions
    data class UpdatedSearch(val text: String, val list: Flow<List<FavoriteHero?>>): VSActions
    data object StartBattle : VSActions
    data object PickRandomCategory : VSActions
    data object CompleteSetup : VSActions
}
