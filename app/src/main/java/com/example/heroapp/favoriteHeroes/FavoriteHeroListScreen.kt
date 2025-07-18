package com.example.heroapp.favoriteHeroes

import androidx.compose.foundation.Image
import coil.request.ImageRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.heroapp.data.room.FavoriteHero


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteHeroListScreen(
    navController: NavController,
    viewModel: FavoriteHeroViewModel = hiltViewModel(),

) {
    val gradientColors = listOf(Color(0xFF243B55), Color(0xFF141E30))

    val favHeroList by viewModel.allFavoriteHeroes.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(140.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF243B55),
                    titleContentColor = Color(0xFF141E30),
                ),
                title = {
                    Text(
                        modifier = Modifier.padding(top = 52.dp),
                        text = "FAVORITES",
                        style = TextStyle(
                                color = Color(0xFFFFC400)
                        ),
                        fontSize = 50.sp,
                        fontWeight = FontWeight.ExtraBold

                    )
                },
            )
        },
    ) { innerPading ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(innerPading)

        ) {
            HeroGrid(heroList = favHeroList, navController = navController, viewModel = viewModel, color = gradientColors)
        }
    }
}
@Composable
fun HeroEntry(
    entry: FavoriteHero,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FavoriteHeroViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(16.dp), ambientColor = Color(0xFFFFC400), spotColor = Color(0xFFFFC400) )
            .clip(RoundedCornerShape(16.dp))
            .aspectRatio(1f)
            .background(
                Color(0xFF8F8F8F)
            )

            .clickable {
                navController.navigate(
                    "hero_detail_screen/${dominantColor.toArgb()}/${entry.id}"
                )
            }
    ) {
        Column {
            val painter =
                rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = entry.image)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            listener { _, loadState ->
                                viewModel.calcDominantColor(loadState.drawable.toBitmap()) { color ->
                                    dominantColor = color
                                }
                            }
                        }).build()
                )

            Image(
                painter = painter,
                contentDescription = entry.image,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = firstCharCap(entry.name),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun HeroGrid(
    heroList: List<FavoriteHero>,
    navController: NavController,
    viewModel: FavoriteHeroViewModel,
    color: List<Color>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(color))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(heroList) { heroEntry ->
                HeroEntry(entry = heroEntry, navController = navController, viewModel = viewModel)
            }
        }
    }
}

fun firstCharCap(input: String): String {
    if (input.isEmpty()) return input
    return input.substring(0, 1).uppercase() + input.substring(1)
}
