package com.example.heroapp.heroDetail

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableOpenTarget
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.heroapp.data.remote.responses.Hero
import com.example.heroapp.util.Resource

@Composable
fun HeroDetailScreen(
    dominantColor: Color,
    heroId: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    heroImageSize: Dp = 200.dp,
    viewModel: HeroDetailViewModel = hiltViewModel()
){
    val heroInfoResult by produceState<Result<Hero>?>(initialValue = null) {
        value = viewModel.getHeroInfo(heroId)
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(dominantColor)
        .padding(bottom = 16.dp)
    ){
        HeroDetailTopSection(navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        heroInfoResult?.let { result ->
            HeroDetailStateWrapper(
                heroInfo = result,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = topPadding + heroImageSize / 2f,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                    .shadow(10.dp, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            )
        }
        Box(contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            heroInfoResult?.let { result ->
                if (result.isSuccess) {
                    val hero = result.getOrNull()
                    if (hero != null) {
                        AsyncImage(
                            model = hero,
                            contentDescription = null,
                            modifier = Modifier
                                .size(heroImageSize)
                                .offset(y = topPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeroDetailTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(Brush.verticalGradient(
                listOf(
                    Color.Black,
                    Color.Transparent
                )
            )
            )
    ){
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun HeroDetailStateWrapper(
    heroInfo: Result<Hero>,
    modifier: Modifier = Modifier,
){
    LaunchedEffect(heroInfo) {
        // Manejar el estado de carga y el resultado de la operación
        if (heroInfo.isSuccess) {
            heroInfo.onSuccess { hero ->
                // Aquí mostrar la información del héroe
            }
        } else if (heroInfo.isFailure) {
            heroInfo.onFailure { error ->
                error.stackTrace
            }
        }
    }
}

