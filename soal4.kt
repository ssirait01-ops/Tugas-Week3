package com.bruce.exercise3.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// ============================================================================
// ENUMS & DATA STRUCTURES FOR SOAL 4: ROCK PAPER SCISSORS (BEST OF N)
// ============================================================================

enum class Rps(val displayName: String, val emoji: String) {
    ROCK("Rock", "✊"),
    PAPER("Paper", "✋"),
    SCISSORS("Scissors", "✌")
}

enum class RpsState {
    INITIAL,
    PICK,
    REVEAL,
    FINISHED
}

enum class RpsResult {
    WIN,
    LOSE,
    DRAW
}

// ============================================================================
// MAIN COMPOSABLE FOR SOAL 4
// ============================================================================

@Composable
fun soal4() {
    RockPaperScissorsGame()
}

@Composable
fun RockPaperScissorsGame() {
    var state by remember { mutableStateOf(RpsState.INITIAL) }
    var selectedBestOfN by rememberSaveable { mutableIntStateOf(5) } // Default Best of 5

    var playerScore by rememberSaveable { mutableIntStateOf(0) }
    var cpuScore by rememberSaveable { mutableIntStateOf(0) }
    var bestMatchScore by rememberSaveable { mutableIntStateOf(0) }

    var playerMove by remember { mutableStateOf<Rps?>(null) }
    var cpuMove by remember { mutableStateOf<Rps?>(null) }
    var roundResult by remember { mutableStateOf<RpsResult?>(null) }

    var buttonOrder by remember { mutableStateOf(Rps.entries.shuffled()) }

    val targetWins = (selectedBestOfN / 2) + 1

    fun evaluateRound(pMove: Rps, cMove: Rps): RpsResult {
        if (pMove == cMove) return RpsResult.DRAW
        return when (pMove) {
            Rps.ROCK -> if (cMove == Rps.SCISSORS) RpsResult.WIN else RpsResult.LOSE
            Rps.PAPER -> if (cMove == Rps.ROCK) RpsResult.WIN else RpsResult.LOSE
            Rps.SCISSORS -> if (cMove == Rps.PAPER) RpsResult.WIN else RpsResult.LOSE
        }
    }

    fun makeMove(pMove: Rps) {
        val cMove = Rps.entries.random()
        val result = evaluateRound(pMove, cMove)

        playerMove = pMove
        cpuMove = cMove
        roundResult = result

        if (result == RpsResult.WIN) playerScore++
        if (result == RpsResult.LOSE) cpuScore++

        state = RpsState.REVEAL
    }

    // Auto-advance after REVEAL state (~700ms)
    LaunchedEffect(state) {
        if (state == RpsState.REVEAL) {
            delay(700)
            if (playerScore >= targetWins || cpuScore >= targetWins) {
                if (playerScore > bestMatchScore) {
                    bestMatchScore = playerScore
                }
                state = RpsState.FINISHED
            } else {
                buttonOrder = Rps.entries.shuffled()
                state = RpsState.PICK
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
            when (state) {
                RpsState.INITIAL -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Rock • Paper • Scissors",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Best of N Match",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            text = "Pilih Format Pertandingan:",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            listOf(3, 5, 7).forEach { n ->
                                FilterChip(
                                    selected = selectedBestOfN == n,
                                    onClick = { selectedBestOfN = n },
                                    label = { Text("Best of $n", fontWeight = FontWeight.Bold) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "First to ${selectedBestOfN / 2 + 1} points wins the match!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )

                        if (bestMatchScore > 0) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Best Score: $bestMatchScore",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }

                    Button(
                        onClick = {
                            playerScore = 0
                            cpuScore = 0
                            buttonOrder = Rps.entries.shuffled()
                            state = RpsState.PICK
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Start Game", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                RpsState.PICK, RpsState.REVEAL -> {
                    // Header Scores
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Best of $selectedBestOfN (First to $targetWins)",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "🧑 You: $playerScore",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("—", style = MaterialTheme.typography.titleLarge)
                                Text(
                                    text = "$cpuScore :CPU 🤖",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Central Arena
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        if (state == RpsState.PICK) {
                            Text(
                                text = "Pick your move!",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🧑 ❓", fontSize = 36.sp)
                                Text("VS", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text("❓ 🤖", fontSize = 36.sp)
                            }
                        } else {
                            // REVEAL STATE
                            Text(
                                text = when (roundResult) {
                                    RpsResult.WIN -> "You Win! 🎉"
                                    RpsResult.LOSE -> "You Lose! 💔"
                                    RpsResult.DRAW -> "Draw! 🤝"
                                    null -> ""
                                },
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = when (roundResult) {
                                    RpsResult.WIN -> Color(0xFF4CAF50)
                                    RpsResult.LOSE -> MaterialTheme.colorScheme.error
                                    RpsResult.DRAW -> MaterialTheme.colorScheme.secondary
                                    null -> MaterialTheme.colorScheme.onBackground
                                }
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🧑 ${playerMove?.emoji ?: ""}", fontSize = 40.sp)
                                Text("VS", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                                Text("${cpuMove?.emoji ?: ""} 🤖", fontSize = 40.sp)
                            }
                        }
                    }

                    // Action Buttons (Shuffled order)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Tombol diacak setiap ronde",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            buttonOrder.forEach { move ->
                                Button(
                                    onClick = { if (state == RpsState.PICK) makeMove(move) },
                                    enabled = state == RpsState.PICK,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(72.dp),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = move.emoji, fontSize = 24.sp)
                                        Text(text = move.displayName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                RpsState.FINISHED -> {
                    val isPlayerWinner = playerScore > cpuScore
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (isPlayerWinner) "YOU WIN THE MATCH! 🎉" else "YOU LOSE THE MATCH 💔",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isPlayerWinner) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
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
                                    text = "Final Match Score",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "🧑 $playerScore — $cpuScore 🤖",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Best Score: $bestMatchScore",
                                    style = MaterialTheme.typography.titleMedium,
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
                            onClick = { state = RpsState.INITIAL },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Exit", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                playerScore = 0
                                cpuScore = 0
                                buttonOrder = Rps.entries.shuffled()
                                state = RpsState.PICK
                            },
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
fun soal4Preview() {
    soal4()
}
