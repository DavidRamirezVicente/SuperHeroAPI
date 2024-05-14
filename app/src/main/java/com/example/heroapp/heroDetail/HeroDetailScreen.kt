package com.example.heroapp.heroDetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.heroapp.R
import com.example.heroapp.data.remote.responses.Appearance
import com.example.heroapp.data.remote.responses.Biography
import com.example.heroapp.data.remote.responses.Powerstats
import com.example.heroapp.domain.model.Hero
import com.example.heroapp.util.HeroParse


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroDetailScreen(
    dominantColor: Color,
    navController: NavController,
    heroImageSize: Dp = 180.dp,
    viewModel: HeroDetailViewModel = hiltViewModel()
) {
    val isFavorite by viewModel.isFavorite.collectAsState()
    val heroInfoResult by produceState<Result<Hero>?>(initialValue = null) {
        value = viewModel.getHeroInfo(viewModel.heroId)
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                heroInfoResult?.getOrNull()?.let {
                    CenterAlignedTopAppBar(
                        modifier = Modifier.padding(top = 10.dp),
                        title = { Text(text = "") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Go back",
                                    modifier = Modifier.size(45.dp))
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                if (isFavorite){
                                    viewModel.deleteFavoriteHero(it.id)
                                }else{
                                    viewModel.saveFavoriteHero(it.id, it.name, it.image)
                                }
                            }) {
                                Icon(imageVector = Icons.Filled.Star,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorite) Color.Yellow else Color.White,
                                    modifier = Modifier.size(45.dp)
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarColors(
                            dominantColor,
                            dominantColor,
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surface
                        ),

                    )
                }
            }

        }
    ) { innerPading ->
        Column(
            modifier = Modifier
                .background(dominantColor)
                .padding(innerPading)
                //.offset(y = -(heroImageSize/2))
        ) {
                heroInfoResult?.let { result ->
                    HeroDetailStateWrapper(
                        heroInfo = result,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            )
                            .shadow(10.dp, RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    )
                }
        }

    }
}
@Composable
fun HeroDetailStateWrapper(
    heroInfo: Result<Hero>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = Modifier
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
            .shadow(10.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        item {
            heroInfo.getOrNull().let {
                if (it != null) {
                    AsyncImage(
                        model = it.image,
                        contentDescription = it.name,
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth()
                    )
                }
            }
            if (heroInfo.isSuccess) {
                HeroDetailSection(
                    heroInfo = heroInfo.getOrThrow(),
                    modifier = modifier
                )
            } else if (heroInfo.isFailure) {
                Text(text = "Error fetching hero data. Please try again later.")
            }
        }
    }
}


@Composable
fun HeroDetailSection(
    heroInfo: Hero,
    modifier: Modifier = Modifier
) {
    //val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            //.fillMaxSize()
            //.verticalScroll(scrollState)
            .padding(top = 77.dp)
    ) {
            Text(
                text = firstCharCap(heroInfo.name),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            HeroRaceSection(info = heroInfo.appearance)
            HeroDetailDataSection(
                heroWeight = heroInfo.appearance.weight[1],
                heroHeight = heroInfo.appearance.height[1]
            )
            HeroBaseStats(powerstat = heroInfo.powerstats, heroParse = HeroParse())
            Spacer(modifier = Modifier.height(16.dp))
            HeroBiography(biography = heroInfo.biography)
    }
}


@Composable
fun HeroRaceSection(info: Appearance) {
    var race = info.race
    if (race.equals("null")) {
        race = "Unknown"
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .padding(horizontal = 8.dp)
                .background(MaterialTheme.colorScheme.primary)
                .height(35.dp)
                .weight(1f)
        ) {
            Text(
                text = firstCharCap(race),
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
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        HeroDetailDataItem(
            dataValue = heroWeight,
            dataUnit = "",
            dataIcon = painterResource(id = R.drawable.weighticon),
            modifier = Modifier.weight(1f)
        )
        Spacer(
            modifier = Modifier
                .size(1.dp, sectionHeight)
                .background(Color.LightGray)
        )
        HeroDetailDataItem(
            dataValue = heroHeight,
            dataUnit = "",
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
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(
                16.dp
            )
    ) {
        Icon(
            painter = dataIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
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
) {

    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            if (statValue == "null") 0f else statValue.toInt() / statMaxValue.toFloat()
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
            .clip(RoundedCornerShape(16.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(RoundedCornerShape(16.dp))
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HeroBaseStats(
    powerstat: Powerstats,
    heroParse: HeroParse,
    animDelayPerItem: Int = 100,
) {
    val maxBaseStat = 100
    var expanded by rememberSaveable { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Powerstats",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { expanded = !expanded },
                    modifier = Modifier
                        .width(100.dp)
                        .height(40.dp)
                ) {
                    Text(if (expanded) "Hide" else "Show")
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = slideInVertically(initialOffsetY = { 40 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { 40 }) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
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
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BiographyItem(
    title: String,
    value: String
) {
    Column(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = value,
            fontSize = 14.sp
        )
    }
}

@Composable
fun HeroBiography(
    biography: Biography
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Biography",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { expanded = !expanded },
                    modifier = Modifier
                        .width(100.dp)
                        .height(40.dp)
                ) {
                    Text(if (expanded) "Hide" else "Show")
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = slideInVertically(initialOffsetY = { 40 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { 40 }) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Spacer(modifier = Modifier.height(4.dp))
                    biography.fullName.let { BiographyItem(title = "Full Name:", value = it) }
                    biography.alterEgos?.let { BiographyItem(title = "AlterEgos:", value = it) }
                    BiographyItem(title = "Aliases:", value = biography.aliases.joinToString())
                    biography.placeOfBirth?.let {
                        BiographyItem(
                            title = "Place of birth:",
                            value = it
                        )
                    }
                    biography.firstAppearance?.let {
                        BiographyItem(
                            title = "First Appearance:",
                            value = it
                        )
                    }
                    BiographyItem(title = "Publisher:", value = biography.publisher)
                    BiographyItem(title = "Alignment:", value = biography.alignment)

                }
            }
        }
    }
}


fun firstCharCap(input: String): String {
    if (input.isEmpty()) return input
    return input.substring(0, 1).uppercase() + input.substring(1)
}



