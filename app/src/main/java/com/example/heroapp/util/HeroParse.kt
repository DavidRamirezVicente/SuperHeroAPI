package com.example.heroapp.util

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import com.example.heroapp.R
import com.example.heroapp.ui.theme.CombatColor
import com.example.heroapp.ui.theme.DurabilityColor
import com.example.heroapp.ui.theme.IntelligColor
import com.example.heroapp.ui.theme.PowerColor
import com.example.heroapp.ui.theme.SpeedColor
import com.example.heroapp.ui.theme.StrengthColor
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

    fun parseStatToIcon(statName: String): Any {
        return when(statName.lowercase(Locale.ROOT)) {
            "intelligence" -> R.drawable.intelligence_removebg_preview
            "strength" -> R.drawable.strength_removebg_preview
            "speed" -> R.drawable.speed_removebg_preview
            "durability" -> R.drawable.yunke
            "power" -> R.drawable.power_removebg_preview
            "combat" -> R.drawable.fist_removebg_preview
            else -> Color.White
        }
    }

}
