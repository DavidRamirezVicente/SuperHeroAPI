package com.example.heroapp.vsHero

import androidx.compose.foundation.Image
import coil.request.ImageRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.heroapp.data.room.FavoriteHero
import com.example.heroapp.domain.VSStates
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFavScreen(
    navController: NavController,
    vsViewModel: VsViewModel = hiltViewModel()
    ) {
    val gradientColors = listOf(Color(0xFF243B55), Color(0xFF141E30))
    val favHeroList by vsViewModel.allFavoriteHeroes.collectAsState(initial = emptyList())
    val currentState by vsViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 15.dp, end = 15.dp),
            ) { newSearchTerm ->
                if (newSearchTerm.isNotEmpty()) {
                    vsViewModel.searchParticipants(newSearchTerm)
                } else {
                    Timber.d("Escribe el nombre de un superheroe")
                }
            }
        }
    ) { innerPading ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradientColors))
                .padding(innerPading)

        ) {
            HeroGrid(heroList = favHeroList, navController = navController, state = currentState, color = gradientColors)
        }
    }
}
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
) {
    var text by remember { mutableStateOf("") }
    Box(modifier = modifier) {
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            label = { Text("Search...") },
            singleLine = true,
            placeholder = { Text("Write a superhero name") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Localized description") },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "Localized description",
                        modifier = Modifier.clickable {
                            text = ""
                        }
                    )
                }
            },
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(text)
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color(0xFFB4B4B4))
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(30.dp))
        )
    }
}
@Composable
fun HeroEntry(
    entry: FavoriteHero,
    navController: NavController,
    modifier: Modifier = Modifier,
    slotId: Int,
    viewModel: VsViewModel = hiltViewModel()
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .aspectRatio(1f)
            .background(
                Color(0xFF8F8F8F)
            )

            .clickable {
                viewModel.selectParticipant(entry, slotId = slotId)
            }
    ) {
        Column {
            val painter =
                rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = entry.image)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
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
    state: VSStates,
    color: List<Color>
) {
    val slotId = when(state){
        is VSStates.SettingUpSearch -> state.slotId
        else -> 1
    }
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
                HeroEntry(entry = heroEntry, navController = navController,  slotId = slotId)
            }
        }
    }
}

fun firstCharCap(input: String): String {
    if (input.isEmpty()) return input
    return input.substring(0, 1).uppercase() + input.substring(1)
}
