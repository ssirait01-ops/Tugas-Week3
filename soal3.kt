package com.bruce.exercise3.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.random.Random

enum class StroopGameState {
    INITIAL,
    COUNTDOWN,
    RUNNING,
    GAME_OVER
}

enum class StroopMode {
    COLOR,
    TEXT
}

enum class StroopColor(val label: String, val displayColor: Color) {
    RED("RED", Color(0xFFE53935)),
    BLUE("BLUE", Color(0xFF1E88E5)),
    GREEN("GREEN", Color(0xFF4CAF50)),
    YELLOW("YELLOW", Color(0xFFFBC02D)),
    PURPLE("PURPLE", Color(0xFF8E24AA)),
    ORANGE("ORANGE", Color(0xFFFB8C00))
}

@Composable
fun soal3() {
    StroopColorMatchingGame()
}

@Composable
fun StroopColorMatchingGame() {
    var gameState by remember { mutableStateOf(StroopGameState.INITIAL) }
    var countdownText by remember { mutableStateOf("3") }

    var score by remember { mutableIntStateOf(0) }
    var bestScore by rememberSaveable { mutableIntStateOf(0) }
    var strikes by remember { mutableIntStateOf(0) }

    var wordColor by remember { mutableStateOf(StroopColor.RED) }
    var inkColor by remember { mutableStateOf(StroopColor.BLUE) }
    var mode by remember { mutableStateOf(StroopMode.COLOR) }
    var isInkOnLeft by remember { mutableStateOf(true) }

    val totalTimeMs = 5000L
    var timeRemainingMs by remember { mutableLongStateOf(totalTimeMs) }
    var questionId by remember { mutableIntStateOf(0) }

    fun generateNewQuestion() {
        val colors = StroopColor.entries
        val newWord = colors.random()
        var newInk = colors.random()
        while (newInk == newWord) {
            newInk = colors.random()
        }
        wordColor = newWord
        inkColor = newInk
        mode = if (Random.nextBoolean()) StroopMode.COLOR else StroopMode.TEXT
        isInkOnLeft = Random.nextBoolean()
        timeRemainingMs = totalTimeMs
        questionId++
    }

    fun handleAnswer(chosenIsInk: Boolean) {
        val isCorrect = when (mode) {
            StroopMode.COLOR -> chosenIsInk
            StroopMode.TEXT -> !chosenIsInk
        }

        if (isCorrect) {
            score++
            if (score > bestScore) {
                bestScore = score
            }
            generateNewQuestion()
        } else {
            strikes++
            if (strikes >= 3) {
                gameState = StroopGameState.GAME_OVER
            } else {
                generateNewQuestion()
            }
        }
    }

    LaunchedEffect(gameState) {
        if (gameState == StroopGameState.COUNTDOWN) {
            countdownText = "3"
            delay(1000)
            countdownText = "2"
            delay(1000)
            countdownText = "1"
            delay(1000)
            countdownText = "Start!"
            delay(700)
            score = 0
            strikes = 0
            generateNewQuestion()
            gameState = StroopGameState.RUNNING
        }
    }

    LaunchedEffect(gameState, questionId) {
        if (gameState == StroopGameState.RUNNING) {
            timeRemainingMs = totalTimeMs
            while (timeRemainingMs > 0) {
                delay(50)
                timeRemainingMs -= 50
            }
            strikes++
            if (strikes >= 3) {
                gameState = StroopGameState.GAME_OVER
            } else {
                generateNewQuestion()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when (gameState) {
                StroopGameState.INITIAL -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Color Word Matching",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Melatih Fokus & Inhibisi Respon (Efek Stroop)",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Aturan Game:",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Mode COLOR → Pilih tombol warna TINTA tulisan.")
                                Text("• Mode TEXT → Pilih tombol NAMA KATA tulisan.")
                                Text("• Game endless sampai salah/timeout 3x.")
                                Text("• Waktu per soal: 5.0 detik.")
                            }
                        }

                        if (bestScore > 0) {
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Best Score: $bestScore",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }

                    Button(
                        onClick = { gameState = StroopGameState.COUNTDOWN },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Start Game", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                StroopGameState.COUNTDOWN -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = countdownText,
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp),
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                StroopGameState.RUNNING -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Score: $score",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Best: $bestScore",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Strikes: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                repeat(3) { index ->
                                    Text(
                                        text = if (index < strikes) "❌ " else "⚪ ",
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val secondsText = String.format(Locale.getDefault(), "%.1f", timeRemainingMs / 1000f)
                        Text(
                            text = "Time Left: ${secondsText}s",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (timeRemainingMs < 1500) Color.Red else MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { (timeRemainingMs.toFloat() / totalTimeMs.toFloat()).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (timeRemainingMs < 1500) Color.Red else MaterialTheme.colorScheme.primary
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (mode == StroopMode.COLOR) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = if (mode == StroopMode.COLOR) "MODE: COLOR (Pilih WARNA TINTA)" else "MODE: TEXT (Pilih NAMA KATA)",
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (mode == StroopMode.COLOR) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.height(36.dp))

                        Text(
                            text = wordColor.label,
                            color = inkColor.displayColor,
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 56.sp),
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val leftLabel = if (isInkOnLeft) inkColor.label else wordColor.label
                        val rightLabel = if (isInkOnLeft) wordColor.label else inkColor.label

                        Button(
                            onClick = { handleAnswer(chosenIsInk = isInkOnLeft) },
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = leftLabel,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { handleAnswer(chosenIsInk = !isInkOnLeft) },
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = rightLabel,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                StroopGameState.GAME_OVER -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "GAME OVER",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(24.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Final Score",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "$score",
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Best Score: $bestScore",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { gameState = StroopGameState.INITIAL },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Exit", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { gameState = StroopGameState.COUNTDOWN },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Restart", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun soal3Preview() {
    soal3()
}
