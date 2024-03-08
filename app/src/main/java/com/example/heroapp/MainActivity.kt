package com.example.heroapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.heroapp.heroList.HeroListScreen
import com.example.heroapp.ui.theme.HeroAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeroAppTheme {
                // Se crea un NavController para manejar la navegación entre pantallas.
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "hero_list_screen") {
                    // Se define el NavHost que contendrá las pantallas de la aplicación.
                    composable("hero_list_screen") {
                        HeroListScreen(navController = navController)
                    }
                    // Pantalla para mostrar los detalles de un héroe.
                    composable(
                        "hero_detail_screen/{dominantColor}/{heroName}",
                        arguments = listOf(
                            navArgument("dominantColor") {
                                type = NavType.IntType
                            },
                            navArgument("heroName") {
                                type = NavType.StringType
                            }
                        )
                        // Se recuperan los argumentos de la pantalla.
                    ) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) } ?: Color.White
                        }
                        val heroName = remember {
                            it.arguments?.getString("heroName")
                        }
                    }
                    composable("fav_hero_list"){

                    }
                }
            }
        }
    }
}