package com.example.heroapp.vsHero

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.heroapp.data.room.FavoriteHero
import com.example.heroapp.domain.VSStates
import com.example.heroapp.ui.theme.HeroAppTheme
import kotlinx.coroutines.flow.compose
import timber.log.Timber

@Composable
fun VsScreen(
    viewModel: VsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is VSStates.NoActiveMatch -> NoActiveMatchScreen { viewModel.startMatch() }
        is VSStates.SettingUpMatch -> SettingUpMatchScreen(state as VSStates.SettingUpMatch)
        is VSStates.SettingUpSearch -> SearchFavScreen()
        is VSStates.SetupComplete -> SetUpCompleteScreen(state = state as VSStates.SetupComplete) {
            viewModel.startFight()
        }

        is VSStates.RollingDice -> RollingDiceScreen(
            viewModel = viewModel,
            state = state as VSStates.RollingDice
        )

        is VSStates.Battling -> BattleScreen(state = state as VSStates.Battling) {
            viewModel.startFight()
        }

        is VSStates.Winner -> WinnerScreen(state as VSStates.Winner){
            viewModel.returnToStart()
        }
    }

}

@Composable
fun NoActiveMatchScreen(startMatch: () -> Unit) {
    BeginButton(onClick = startMatch)
}

@Composable
fun SettingUpMatchScreen(state: VSStates.SettingUpMatch) {
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
        VSHeroSection(modifier = Modifier, state.firstContestant, state.secondContestant)
    }
}

@Composable
fun SetUpCompleteScreen(state: VSStates.SetupComplete, onFightClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(bottom = 25.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 50.dp)
        ) {
            VSHeroSection(
                Modifier.padding(top = 50.dp),
                firstHero = state.firstContestant,
                secondHero = state.secondContestant,
            )
        }
        FightButton(onFightClick)
    }

}


@Composable
fun RollingDiceScreen(viewModel: VsViewModel, state: VSStates.RollingDice) {
    val value by viewModel.diceResult.collectAsState()
    val powerstat = viewModel.getPowerStat(value)
    val isEnabled by viewModel.isRollEnabled.collectAsState()

    Column(
        modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Category: ",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = powerstat,
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 300.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .clickable(enabled = isEnabled) {
                        viewModel.rollDice()
                },
            painter = painterResource(viewModel.imageResource(value)),
            contentDescription = null
        )
    }
}


@Composable
fun BattleScreen(state: VSStates.Battling,onFightClick: () -> Unit) {
        Column(
            modifier = Modifier
                .padding(top = 50.dp)
        ) {
            VSHeroSection(
                firstHero = state.firstContestant,
                secondHero = state.secondContestant,
            )
            ScoreSection(
                state
            )
            HeroVsStats(state = state)
            FightButton(onFightClick)
        }
}

@Composable
fun WinnerScreen(state: VSStates.Winner, onReturnClick: () -> Unit) {
    val hero = state.hero

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopHeroWin(entry = hero)
        WinnerImage(entry = hero)
        ReturnButton(onReturnClick)
    }
}



@Composable
fun VSHeroSection(
    modifier: Modifier = Modifier,
    firstHero: FavoriteHero?,
    secondHero: FavoriteHero?
) {
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
            slotId = 2
        )
    }
}

@Composable
fun ScoreSection(state: VSStates.Battling) {
        val winsFirstContestant = state.winsFirstContestant
        val winsSecondContestant = state.winsSecondContestant
        Timber.d("$winsFirstContestant, $winsSecondContestant")
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ScoreCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp),
                score = winsFirstContestant
            )
            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {

            }
            ScoreCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp),
                score = winsSecondContestant
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
    slotId: Int?
) {
    val viewModel: VsViewModel = hiltViewModel()

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
        modifier = modifier.padding(start = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        ElevatedCard(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .size(width = 100.dp, height = 100.dp)
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
fun FightButton(startFight: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = startFight,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            colors = ButtonDefaults.buttonColors(),
            modifier = Modifier.padding(16.dp)
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

    val statColor = when {
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
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(statColor)
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
    state: VSStates.Battling
) {
    Surface(
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(150.dp)
    ) {
        val rounds = state.rounds
        LazyColumn() {
            item {
                Text(
                    text = "Rounds",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }

            itemsIndexed(rounds) { index, round ->
                HeroStats(
                    statIcon = round.category,
                    firstHeroStatValue = round.statValue1,
                    secondHeroStatValue = round.statValue2,
                    statMaxValue = 100,
                    animDelay = index * animDelayPerItem
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}

@Composable
fun TopHeroWin(entry: FavoriteHero?) {
    Column(
        modifier = Modifier
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = entry?.name ?: "¡DRAW!",
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = if (entry != null) "WINS!" else "",
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun WinnerImage(entry: FavoriteHero?) {
    entry?.let {
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(it.image)
                .crossfade(true)
                .build()
        )
        Image(
            painter = painter,
            contentDescription = it.name,
            modifier = Modifier
                .size(200.dp),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
    }
}

@Composable
fun ReturnButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 12.dp ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text("RETURN")
    }
}


@Preview
@Composable
private fun PreviewHeroHeader() {
    HeroAppTheme {
    }
}
