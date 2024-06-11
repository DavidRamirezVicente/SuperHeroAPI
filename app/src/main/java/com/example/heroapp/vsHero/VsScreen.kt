package com.example.heroapp.vsHero

import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.heroapp.R
import com.example.heroapp.data.room.FavoriteHero
import com.example.heroapp.domain.VSStates
import com.example.heroapp.domain.model.PowerStats
import com.example.heroapp.ui.theme.HeroAppTheme
import com.example.heroapp.util.HeroParse
import kotlin.math.absoluteValue

@Composable
fun VsScreen(
    navController: NavController,
    viewModel: VsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is VSStates.NoActiveMatch -> NoActiveMatchScreen { viewModel.startMatch() }
        is VSStates.SettingUpMatch -> SettingUpMatchScreen(viewModel, state as VSStates.SettingUpMatch)
        is VSStates.SettingUpSearch -> SearchFavScreen(navController)
        is VSStates.SetupComplete -> SetUpCompleteScreen(viewModel = viewModel, state = state as VSStates.SetupComplete) {
        }
        is VSStates.RollingDice -> RollingDiceScreen(viewModel = viewModel, state = state as VSStates.RollingDice)
        is VSStates.Battling -> BattleScreen(viewModel = viewModel, state = state as VSStates.Battling)
    }

}

@Composable
fun NoActiveMatchScreen(startMatch: () -> Unit) {
    BeginButton(onClick = startMatch)
}

@Composable
fun SettingUpMatchScreen(viewModel: VsViewModel, state: VSStates.SettingUpMatch) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "SELECT YOUR",
            textAlign = TextAlign.Center,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 50.dp)
        )
        Text(
            text = "HEROES",
            textAlign = TextAlign.Center,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
           modifier = Modifier.padding(bottom = 50.dp)
        )
        VSHeroSection(modifier = Modifier,state.firstContestant, state.secondContestant, viewModel)
    }
}

@Composable
fun SetUpCompleteScreen(viewModel: VsViewModel, state: VSStates.SetupComplete, function: () -> Unit) {
    Column(modifier = Modifier
        .padding(bottom = 25.dp)
    ) {
        Column(modifier = Modifier
            .padding(top = 50.dp)
        ) {
            VSHeroSection(
                Modifier.padding(top = 50.dp),
                firstHero = state.firstContestant,
                secondHero = state.secondContestant,
                viewModel = viewModel
            )
        }
        FightButton(
            Modifier
                .fillMaxSize()
        ) {
            viewModel.startFight()
        }
    }

}


@Composable
fun RollingDiceScreen(modifier: Modifier = Modifier, viewModel: VsViewModel, state: VSStates.RollingDice) {
    val value by viewModel.diceResult.collectAsState()
    val powerstatsMap = mapOf(
        0 to "",
        1 to "Combat",
        2 to "Durability",
        3 to "Intelligence",
        4 to "Power",
        5 to "Speed",
        6 to "Strength"
    )
    val imageResource = when(value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
    val powerstat = powerstatsMap[value]

    Column(modifier = Modifier
        .padding(top = 50.dp)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        Text(text = "Category: ", fontSize = 50.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(text = "$powerstat", fontSize = 30.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 300.dp),
        verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .clickable {
                    viewModel.rollDice()
                    viewModel.calculateScores(
                        firstHero = state.firstContestant,
                        secondHero = state.secondContestant
                    )
                },

            painter = painterResource(imageResource),
            contentDescription = viewModel.diceResult.toString()
        )
    }
}

@Composable
fun BScreen(viewModel: VsViewModel, state: VSStates.Battling, modifier: Modifier = Modifier) {
    BScreenBody {
        viewModel.onCellSelect(it)
    }
}

@Composable
fun BScreenBody(modifier: Modifier = Modifier, onHeroSelect: (Int) -> Unit) {

}

@Composable
fun BattleScreen(viewModel: VsViewModel, state: VSStates.Battling){
    Column(modifier = Modifier
        .padding(bottom = 25.dp)
    ) {
        Column(modifier = Modifier
            .padding(top = 50.dp)
        ) {
            VSHeroSection(
                Modifier.padding(top = 50.dp),
                firstHero = state.firstContestant,
                secondHero = state.secondContestant,
                viewModel = viewModel
            )
        }
        ScoreSection(
            modifier = Modifier,
            firstHero = state.firstContestant,
            secondHero = state.secondContestant,
            viewModel = viewModel
        )
        HeroVsStats(viewModel = viewModel)
        FightButton(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            viewModel.startFight()
        }
    }
}

@Composable
fun VSHeroSection(modifier: Modifier, firstHero: FavoriteHero?, secondHero: FavoriteHero?, viewModel: VsViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        HeroCard(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            entry = firstHero,
            viewModel = viewModel,
            slotId = 1
        )
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = "VS",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
        HeroCard(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            entry = secondHero,
            viewModel = viewModel,
            slotId = 2
        )
    }
}

@Composable
fun ScoreSection(modifier: Modifier, firstHero: FavoriteHero?, secondHero: FavoriteHero?, viewModel: VsViewModel) {
    val score1 by viewModel.hero1Score.collectAsState()
    val score2 by viewModel.hero2Score.collectAsState()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ScoreCard(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            score = score1
        )
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
        }
        ScoreCard(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            score = score2
        )
    }
}

@Composable
fun BeginButton(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onClick,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
        ) {
            Text(
                text = "Begin",
                textAlign = TextAlign.Center,
            )
        }

    }
}

@Composable
fun HeroCard(
    entry: FavoriteHero?,
    modifier: Modifier = Modifier,
    viewModel: VsViewModel,
    slotId: Int
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        ElevatedCard(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .size(width = 150.dp, height = 250.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { viewModel.onCellSelect(slotId) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF8F8F8F)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                entry?.let {
                    val painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = it.image)
                            .apply {
                                crossfade(true)
                            }
                            .build()
                    )

                    Image(
                        painter = painter,
                        contentDescription = it.name,
                        modifier = Modifier
                            .size(180.dp)
                            .padding(16.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = it.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreCard(
    modifier: Modifier = Modifier,
    score: Int
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        ElevatedCard(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .size(width = 150.dp, height = 150.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF8F8F8F)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                    Text(
                        text = score.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }

@Composable
fun FightButton(modifier: Modifier, startFight: ()-> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = startFight,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            colors = ButtonDefaults.buttonColors()
        ) {
            Text("FIGHT")
        }

    }
}



@Composable
fun HeroStats(
    statIcon: Int,
    firstHeroStatValue: Int,
    secondHeroStatValue: Int,
    statMaxValue: Int,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    val maxStat = maxOf(firstHeroStatValue, secondHeroStatValue)

    val stat1Color = when {
        firstHeroStatValue > secondHeroStatValue -> Color.Blue
        firstHeroStatValue < secondHeroStatValue -> Color.Red
        else -> Color.Green
    }

    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            maxStat / statMaxValue.toFloat()
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
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(stat1Color)
                .padding(horizontal = 8.dp)
        ) {
            Image(
                modifier = Modifier.size(28.dp),
                painter = painterResource(statIcon),
                contentDescription = null
            )
            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HeroVsStats(
    animDelayPerItem: Int = 100,
    viewModel: VsViewModel
) {
    val diceValue = viewModel.diceResult.collectAsState().value
    val imageResource = when(diceValue) {
        1 -> R.drawable.fist_removebg_preview
        2 -> R.drawable.yunke
        3 -> R.drawable.intelligence_removebg_preview
        4 -> R.drawable.power_removebg_preview
        5 -> R.drawable.speed_removebg_preview
        else -> R.drawable.strength_removebg_preview
    }
    val hero1 by viewModel.firstHeroStatValue.collectAsState()
    val hero2 by viewModel.secondHeroStatValue.collectAsState()
    val maxBaseStat = 100

    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp, bottom = 30.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item{
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Rounds",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp

                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                            HeroStats(
                                statIcon = imageResource,
                                firstHeroStatValue = hero1 ,
                                secondHeroStatValue = hero2,
                                statMaxValue = maxBaseStat,
                                animDelay = animDelayPerItem
                            )

                        }
                    }
                }
            }

        }
    }



@Preview
@Composable
private fun PreviewHeroHeader() {
    HeroAppTheme {
    }
}
