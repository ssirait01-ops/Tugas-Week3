package com.bruce.exercise3.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruce.exercise3.R
import java.util.Locale

@Composable
fun soal2() {
    var coins by remember { mutableDoubleStateOf(0.0) }
    var clickValue by remember { mutableDoubleStateOf(1.0) }
    var upgradeCost by remember { mutableDoubleStateOf(10.0) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val nextClickValue = clickValue * 1.5
    val coinsNeeded = if (coins < upgradeCost) upgradeCost - coins else 0.0

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_cat),
            contentDescription = "Background Cat",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text(
                    text = "Coins: ${coins.toInt()}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "+${String.format(Locale.getDefault(), "%.1f", clickValue)} per tap",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }

            val catImageRes = if (isPressed) R.drawable.touch_cat else R.drawable.untouch_cat

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(250.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        coins += clickValue
                    }
            ) {
                Image(
                    painter = painterResource(id = catImageRes),
                    contentDescription = if (isPressed) "Cat Touch" else "Cat Untouch",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Upgrade Click Power",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Next Power: +${String.format(Locale.getDefault(), "%.1f", nextClickValue)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val canUpgrade = coins >= upgradeCost
                    Button(
                        onClick = {
                            if (canUpgrade) {
                                coins -= upgradeCost
                                clickValue = nextClickValue
                                upgradeCost *= 2.0
                            }
                        },
                        enabled = canUpgrade,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (canUpgrade) {
                            Text(text = "Upgrade (Cost: ${upgradeCost.toInt()} coins)")
                        } else {
                            Text(text = "Need ${coinsNeeded.toInt()} more coins (Cost: ${upgradeCost.toInt()})")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun soal2Preview() {
    soal2()
}
