package com.example.heroapp.util

import androidx.compose.ui.graphics.Color
import com.example.heroapp.data.remote.responses.Powerstats
import com.example.heroapp.ui.theme.CombatColor
import com.example.heroapp.ui.theme.DurabilityColor
import com.example.heroapp.ui.theme.IntelligColor
import com.example.heroapp.ui.theme.PowerColor
import com.example.heroapp.ui.theme.SpeedColor
import com.example.heroapp.ui.theme.StrengthColor
import com.squareup.kotlinpoet.MemberName.Companion.member
import java.util.Locale

class HeroParse {

    fun parseStatToColor(statName: String): Color {
        return when(statName.lowercase(Locale.ROOT)) {
            "intelligence" -> IntelligColor
            "strength" -> StrengthColor
            "speed" -> SpeedColor
            "durability" -> DurabilityColor
            "power" -> PowerColor
            "combat" -> CombatColor
            else -> Color.White
        }
    }

}