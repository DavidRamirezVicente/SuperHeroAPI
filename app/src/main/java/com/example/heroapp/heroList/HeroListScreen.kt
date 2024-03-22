package com.example.heroapp.heroList

import androidx.compose.foundation.Image
import coil.request.ImageRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import com.example.heroapp.data.remote.responses.models.HeroListEntry

@Composable
fun HeroListScreen(
    navController: NavController,
    viewModel: HeroListViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize( )
    ) {
        var searchTerm by remember { mutableStateOf("") }

        Column {
            Spacer(modifier = Modifier.height(20.dp))
            //TODO app logo
            SearchBar(
                hint = "Search",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onSearch = { newSearchTerm ->
                    searchTerm = newSearchTerm
                    viewModel.loadHeroList(newSearchTerm)
                }
            )
            HeroGrid(viewModel = viewModel, navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        )
        if (text.isEmpty()) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun HeroEntry(
    entry: HeroListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HeroListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember { mutableStateOf(defaultDominantColor) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .clickable {
                navController.navigate(
                    "hero_detail_screen/${dominantColor.toArgb()}/${entry.id}"
                )
            }
    ) {
        Column {
            /*val request = ImageRequest.Builder(LocalContext.current)
                .data(entry.imageUrl)
                .target{
                    viewModel.calcDominantColor(it){color ->
                        dominantColor = color
                    }
                }
                .build()*/
            val painter =
                rememberAsyncImagePainter(ImageRequest.Builder
                    (LocalContext.current).data(data = entry.imageUrl)
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
                contentDescription = entry.heroName,
                modifier = Modifier
                    .fillMaxSize()
            )

            Text(
                text = entry.heroName,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun HeroGrid(
    viewModel: HeroListViewModel,
    navController: NavController
) {
    val heroList = viewModel.heroList

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(heroList) { heroEntry ->
            HeroEntry(entry = heroEntry, navController = navController, viewModel = viewModel)
        }
    }
}

@Preview
@Composable
fun HeroListScreenPreview() {
    val navController = rememberNavController()
    HeroListScreen(navController = navController)
}