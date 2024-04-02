package com.example.heroapp.heroDetail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.heroapp.R
import com.example.heroapp.data.remote.responses.Appearance
import com.example.heroapp.data.remote.responses.Hero
import com.example.heroapp.data.remote.responses.Powerstats
import com.example.heroapp.util.HeroParse
import java.util.Locale


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
                    .padding(
                        top = topPadding + heroImageSize / 2f,
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
                            model = hero.image,
                            contentDescription = hero.name,
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
            ))
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
    Box(
        contentAlignment = Alignment.TopEnd
    ){
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    Icons.Filled.Star
                }
        )
    }
}

@Composable
fun HeroDetailStateWrapper(
    heroInfo: Result<Hero>,
    modifier: Modifier = Modifier,
){
    if (heroInfo.isSuccess){
        HeroDetailSection(
            heroInfo =  heroInfo.getOrThrow(),
            modifier = modifier
                .offset(y = (-20).dp))

    } else if (heroInfo.isFailure){
        Text(text = "Error fetching hero data. Please try again later.")
    }
}


@Composable
fun HeroDetailSection(
    heroInfo: Hero,
    modifier: Modifier = Modifier
){
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${heroInfo.id} #${firstCharCap(heroInfo.name)}",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.surface
        )
        HeroRaceSection(info = heroInfo.appearance)
        HeroDetailDataSection(
            heroWeight = heroInfo.appearance.weight[1],
            heroHeight = heroInfo.appearance.height[1]
        )
        HeroBaseStats(powerstat = heroInfo.powerstats, heroParse = HeroParse())

    }
}

@Composable
fun HeroRaceSection(info: Appearance){
    Row(
        verticalAlignment =  Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .padding(horizontal = 8.dp)
                .background(MaterialTheme.colorScheme.onSurface)
                .height(35.dp)
        ){
            Text(
                text = firstCharCap(info.race),
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun HeroDetailDataSection(
    heroWeight: String,
    heroHeight: String,
    sectionHeight: Dp = 80.dp
){
    Row(modifier = Modifier
        .fillMaxWidth()) {
        HeroDetailDataItem(dataValue = heroHeight,
            dataUnit = "Kg",
            dataIcon = painterResource(id = R.drawable.weighticon),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier
            .size(1.dp, sectionHeight)
            .background(Color.LightGray))
        HeroDetailDataItem(
            dataValue = heroWeight,
            dataUnit = "Cm",
            dataIcon = painterResource(id = R.drawable.baseline_height),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun HeroDetailDataItem(
    dataValue: String,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(painter = dataIcon, contentDescription = null, tint = MaterialTheme.colorScheme.surface)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun HeroStats(
    statName: String,
    statValue: String,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
){
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed){
            statValue.toInt() / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        ), label = ""
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = (curPercent.value * statMaxValue).toInt().toString(),
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun HeroBaseStats(
    powerstat: Powerstats,
    heroParse: HeroParse,
    animDelayPerItem: Int = 100
){
    val maxBaseStat = 100

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Base stats:",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.surface
        )
        Spacer(modifier = Modifier.height(4.dp))

        val properties = listOf(
            "Combat" to powerstat.combat,
            "Durability" to powerstat.durability,
            "Intelligence" to powerstat.intelligence,
            "Power" to powerstat.power,
            "Speed" to powerstat.speed,
            "Strength" to powerstat.strength
        )

        for ((statName, statValue) in properties) {
            val statColor = heroParse.parseStatToColor(statName)
            HeroStats(
                statName = statName,
                statValue = statValue,
                statMaxValue = maxBaseStat,
                statColor = statColor,
                animDelay = animDelayPerItem
            )
            Spacer(modifier = Modifier
                .height(8.dp))
        }
    }
}
fun firstCharCap(input: String): String {
    if (input.isEmpty()) return input
    return input.substring(0, 1).uppercase() + input.substring(1)
}


