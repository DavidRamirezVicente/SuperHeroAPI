package com.example.heroapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.heroapp.heroDetail.HeroDetailScreen
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
                        "hero_detail_screen/{dominantColor}/{heroId}",
                        arguments = listOf(
                            navArgument("dominantColor") {
                                type = NavType.IntType
                            },
                            navArgument("heroId") {
                                type = NavType.StringType
                            }
                        )
                        // Se recuperan los argumentos de la pantalla.
                    ) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) } ?: Color.White
                        }
                        val heroId = remember {
                            it.arguments?.getString("heroId")
                        }
                        if (heroId != null) {
                            HeroDetailScreen(
                                dominantColor = dominantColor,
                                heroId = heroId,
                                navController = navController
                            )
                        }
                    }
                    composable("fav_hero_list") {

                    }

                }

            }
        }
    }
    /*sealed class Screen(val route: String, @StringRes val resourceId: Int) {
        object HeroDetail : Screen("profile", R.string.profile)
        object HeroList : Screen("herolist", R.string.friends_list)

        object FavHeroes : Screen("favheroes", R.string.fav_heroes)
    }
    val items = listOf(
        Screen.HeroList,
        Screen.HeroDetail,
        Screen.FavHeroes
    )


    Scaffold(
    bottomBar = {
        BottomNavigation {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            items.forEach { screen ->
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                    label = { Text(stringResource(screen.resourceId)) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Profile.route, Modifier.padding(innerPadding)) {
            composable(Screen.Profile.route) { Profile(navController) }
            composable(Screen.FriendsList.route) { FriendsList(navController) }
        }
    }*/
}