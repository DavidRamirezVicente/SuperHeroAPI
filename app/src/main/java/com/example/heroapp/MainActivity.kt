package com.example.heroapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.heroapp.heroDetail.HeroDetailScreen
import com.example.heroapp.heroList.HeroListScreen
import com.example.heroapp.ui.theme.HeroAppTheme
import dagger.hilt.android.AndroidEntryPoint
data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeroAppTheme {
                val navController = rememberNavController()

                val items = listOf(
                    BottomNavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home
                    ),
                    BottomNavigationItem(
                        title = "Favorites",
                        selectedIcon = Icons.Filled.Favorite,
                        unselectedIcon = Icons.Outlined.Favorite
                    ),
                    BottomNavigationItem(
                        title = "VS",
                        selectedIcon = Icons.Filled.Face,
                        unselectedIcon = Icons.Outlined.Face
                    )
                )

                // Composable de la barra de navegación
                Scaffold(
                    bottomBar = {
                        NavigationBar() {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = {
                                        when(index) {
                                            0->
                                            Icon(
                                                if (currentRoute == "hero_list_screen") item.selectedIcon else item.unselectedIcon,
                                                contentDescription = null
                                            )
                                            1 ->
                                            Icon(
                                                if (currentRoute == "fav_hero_list") item.selectedIcon else item.unselectedIcon,
                                                contentDescription = null
                                            )
                                            2 ->
                                            Icon(
                                                if (currentRoute == "versus") item.selectedIcon else item.unselectedIcon,
                                                contentDescription = null
                                            )
                                        }

                                    },
                                    label = { Text(text = item.title) },
                                    selected = currentRoute == when (index) {
                                        0 -> "hero_list_screen"
                                        1 -> "fav_hero_list"
                                        2 -> "versus"
                                        else -> "hero_list_screen"
                                    },
                                    onClick = {
                                        when (index) {
                                            0 -> navController.navigate("hero_list_screen")
                                            1 -> navController.navigate("fav_hero_list")
                                            2 -> navController.navigate("versus")
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "hero_list_screen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("hero_list_screen") {
                            HeroListScreen(navController = navController)
                        }
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
                        composable("versus") {
                        }
                    }
                }
            }
        }
    }
}

