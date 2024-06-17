package com.example.heroapp.util

import com.example.heroapp.data.room.FavoriteHero
import com.example.heroapp.domain.VSActions
import com.example.heroapp.domain.VSStates
import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalCoroutinesApi::class)
class StateMachine : FlowReduxStateMachine<VSStates, VSActions>(initialState = VSStates.NoActiveMatch) {
    init {
        spec {
            inState<VSStates.NoActiveMatch> {
                on<VSActions.Begin> { _, state ->
                    state.override { VSStates.SettingUpMatch(null, null) }
                }
            }

            inState<VSStates.SettingUpMatch> {
                on<VSActions.SearchParticipant> { action, state ->
                    state.override {
                        VSStates.SettingUpSearch(state.snapshot,"", emptyFlow<List<FavoriteHero?>>(),action.slotId)
                    }
                }
            }

            inState<VSStates.SettingUpSearch> {
                on<VSActions.UpdatedSearch> { action, state ->
                    state.mutate { copy(queryText = action.text, candidatesToPick = action.list) }
                }
                on<VSActions.SelectParticipant> { action, state ->
                    val updatedState = updateContestantSelection(state.snapshot, action.hero, state.snapshot.slotId)
                    state.override {
                        if (updatedState.firstContestant != null && updatedState.secondContestant != null) {
                            VSStates.SetupComplete(
                                updatedState.firstContestant,
                                updatedState.secondContestant,
                            )
                        } else {
                            updatedState
                        }
                    }
                }

            }
            inState<VSStates.SetupComplete> {
                on<VSActions.StartBattle> { _, state ->
                    val battling = VSStates.Battling(firstContestant = state.snapshot.firstContestant,
                        secondContestant = state.snapshot.secondContestant, 0,0, emptyList())
                    state.override { VSStates.RollingDice(battling,state.snapshot.firstContestant, state.snapshot.secondContestant) }
                }
            }
            inState<VSStates.RollingDice> {
                on<VSActions.SetRoundResult> { action, state ->
                    val roundResult = VSStates.RoundResult(
                        category = action.category,
                        statValue1 = action.statValue1,
                        statValue2 = action.statValue2
                    )
                    val updatedRounds = state.snapshot.previousState.rounds.toMutableList().apply{
                        add(roundResult)
                    }
                    state.override {
                        VSStates.Battling(
                            firstContestant = state.snapshot.firstContestant,
                            secondContestant = state.snapshot.secondContestant,
                            winsFirstContestant = state.snapshot.previousState.winsFirstContestant,
                            winsSecondContestant = state.snapshot.previousState.winsSecondContestant,
                            rounds = updatedRounds
                        )
                    }
                }
            }

            inState<VSStates.Battling> {
                on<VSActions.StartBattle> { _, state ->
                    state.override {
                        VSStates.RollingDice(
                            previousState = state.snapshot,
                            firstContestant = state.snapshot.firstContestant,
                            secondContestant = state.snapshot.secondContestant
                        )
                    }
                }
            }

        }
    }
    private fun updateContestantSelection(
        currentState: VSStates.SettingUpSearch,
        selectedHero: FavoriteHero,
        slotId: Int
    ): VSStates.SettingUpMatch {
        return if (slotId == 1) {
            currentState.previousState.copy(firstContestant = selectedHero)
        } else {
            currentState.previousState.copy(secondContestant = selectedHero)
        }
    }
}
