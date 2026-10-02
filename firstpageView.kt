package com.bruce.exercise3.view

import android.R.attr.contentDescription
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.random.Random
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import com.bruce.exercise3.R

enum class GamePhase { INITIAL, WAITING, READY, TRIAL_RESULT, FINAL }

data class TrialData(
    val trialNumber: Int,
    val reactionTimeMs: Long? = null,
    val isSuccess: Boolean = true
)

data class ResultStyle(val message: String, val imageRes: Int, val color: Color)

fun getResultStyle(avgTime: Long, hasSuccess: Boolean): ResultStyle = when {
    !hasSuccess -> ResultStyle("ALL TRIALS FAILED!", R.drawable.fornoob, Color(0xFFEF6C35))
    avgTime < 180 -> ResultStyle("DANG YOU ARE SO FAST BRO!", R.drawable.cih, Color(0xFF6BE080))
    avgTime < 280 -> ResultStyle("YOUR REFLEX IS GOOD", R.drawable.prettygood, Color(0xFF4A90E8))
    avgTime < 450 -> ResultStyle("MEH LIKE OTHER PERSON", R.drawable.dang, Color(0xFFF29B38))
    else -> ResultStyle("YOU LIKE A SNAIL BRO", R.drawable.tooslow, Color(0xFFEF6C35))
}

@Composable
fun FirstPageView() {
    var gameState by remember { mutableStateOf(GamePhase.INITIAL) }
    var currentTrial by remember { mutableStateOf(1) }
    val trials = remember { mutableStateListOf<TrialData>() }
    var startTime by remember { mutableStateOf(0L) }
    var currentReactionTime by remember { mutableStateOf(0L) }
    var currentTrialSuccess by remember { mutableStateOf(true) }
    var delayJob by remember { mutableStateOf<Job?>(null) }

    val successfulTrials = trials.filter { it.isSuccess && it.reactionTimeMs != null }
    val avgTime = if (successfulTrials.isNotEmpty()) {
        successfulTrials.map { it.reactionTimeMs!! }.average().toLong()
    } else 0L
    val resultStyle = getResultStyle(avgTime, successfulTrials.isNotEmpty())

    val backgroundColor = when (gameState) {
        GamePhase.INITIAL -> MaterialTheme.colorScheme.background
        GamePhase.WAITING -> Color(0xFFD32F2F)
        GamePhase.READY -> Color(0xFF388E3C)
        GamePhase.TRIAL_RESULT -> MaterialTheme.colorScheme.surfaceVariant
        GamePhase.FINAL -> resultStyle.color
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clickable(
                enabled = gameState == GamePhase.WAITING || gameState == GamePhase.READY || gameState == GamePhase.FINAL
            ) {
                when (gameState) {
                    GamePhase.WAITING -> {
                        delayJob?.cancel()
                        currentTrialSuccess = false
                        currentReactionTime = 0L
                        trials.add(TrialData(currentTrial, null, false))
                        gameState = GamePhase.TRIAL_RESULT
                    }

                    GamePhase.READY -> {
                        val reaction = System.currentTimeMillis() - startTime
                        currentReactionTime = reaction
                        currentTrialSuccess = true
                        trials.add(TrialData(currentTrial, reaction, true))
                        gameState = GamePhase.TRIAL_RESULT
                    }

                    GamePhase.FINAL -> {
                        trials.clear()
                        currentTrial = 1
                        gameState = GamePhase.WAITING
                    }

                    else -> {}
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            when (gameState) {
                GamePhase.INITIAL -> {
                    Icon(
                        imageVector = Icons.Filled.FlashOn,
                        contentDescription = "Petir",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Reaction Test",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Test your reflexes across 3 trials.\nTap when the screen turns GREEN!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            trials.clear()
                            currentTrial = 1
                            gameState = GamePhase.WAITING
                        },
                        modifier = Modifier.height(50.dp)
                    ) {
                        Text(text = "Start Game", fontSize = 18.sp)
                    }
                }

                GamePhase.WAITING -> {
                    LaunchedEffect(currentTrial) {
                        val randomDelay = Random.nextLong(500, 4500)
                        delay(randomDelay)
                        startTime = System.currentTimeMillis()
                        gameState = GamePhase.READY
                    }

                    Text(
                        text = "Trial $currentTrial / 3",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "WAIT FOR GREEN...",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                GamePhase.READY -> {
                    Text(
                        text = "Trial $currentTrial / 3",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "CLICK NOW!",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                GamePhase.TRIAL_RESULT -> {
                    LaunchedEffect(currentTrial) {
                        delay(1200)
                        if (currentTrial < 3) {
                            currentTrial++
                            gameState = GamePhase.WAITING
                        } else {
                            gameState = GamePhase.FINAL
                        }
                    }

                    if (currentTrialSuccess) {
                        Text(
                            text = "Trial $currentTrial Result",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$currentReactionTime ms",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = "Trial $currentTrial Failed!"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You clicked too soon!",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                GamePhase.FINAL -> {
                    Text(
                        text = resultStyle.message,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Image(
                        painter = painterResource(id = resultStyle.imageRes),
                        contentDescription = "Result image",
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Average: $avgTime ms",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Click to Start New Test",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Trial Results",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1976D2)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                trials.forEach { trial ->
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "${trial.trialNumber}",
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4CAF50)
                                        )
                                        Text(
                                            text = if (trial.isSuccess && trial.reactionTimeMs != null)
                                                "${trial.reactionTimeMs}ms" else "Failed",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = if (trial.isSuccess) Color.Black else Color.Red
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Average Score",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1976D2)
                            )
                            Text(
                                text = "${avgTime}ms",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEF6C35)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun FirstPagePreview() {
    FirstPageView()
}
