package com.bruce.exercise3.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bonus2() {
    val context = LocalContext.current

    var spotWisata by remember { mutableStateOf("Fjellheisen Cable Car") }
    var pengalaman by remember {
        mutableStateOf("Pemandangan langit malam hijau aurora spektakuler dari puncak gunung...")
    }
    var catatan by remember { mutableStateOf("") }
    var selectedKepuasan by remember { mutableStateOf("Luar Biasa ★") }

    val kepuasanOptions = listOf("Biasa", "Seru", "Luar Biasa ★")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val message = if (spotWisata.isNotBlank()) {
                        "Jurnal '$spotWisata' berhasil disimpan!"
                    } else {
                        "Jurnal berhasil disimpan!"
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                },
                containerColor = Color(0xFF539DF3),
                contentColor = Color.Black,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Simpan Jurnal",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        containerColor = Color(0xFF0F1422)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "15:00", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "98%", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF22519B),
                                Color(0xFF3877CE)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = "LOG PERJALANAN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB0CFFF),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tromsø, Norway",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Ekspedisi Aurora Borealis",
                            fontSize = 13.sp,
                            color = Color(0xFFD6E4FF)
                        )
                    }

                    Icon(
                        imageVector = Icons.Outlined.AcUnit,
                        contentDescription = "Snowflake Icon",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            CustomInputField(
                label = "SPOT WISATA FAVORIT",
                value = spotWisata,
                onValueChange = { spotWisata = it },
                singleLine = true,
                minLines = 1,
                placeholder = "Nama spot wisata..."
            )

            CustomInputField(
                label = "APA YANG PALING KAMU NIKMATI?",
                value = pengalaman,
                onValueChange = { pengalaman = it },
                singleLine = false,
                minLines = 3,
                placeholder = "Tuliskan pengalamanmu di sini..."
            )

            CustomInputField(
                label = "CATATAN TAMBAHAN / PERLENGKAPAN",
                value = catatan,
                onValueChange = { catatan = it },
                singleLine = false,
                minLines = 2,
                placeholder = "(Ketik perlengkapan ekstra di sini...)"
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "TINGKAT KEPUASAN:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7E8A9F),
                    letterSpacing = 1.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    kepuasanOptions.forEach { option ->
                        val isSelected = option == selectedKepuasan
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) Color(0xFF539DF3) else Color(0xFF1D2436)
                                )
                                .clickable { selectedKepuasan = option }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.Black else Color(0xFF8C9BB2)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Status: Draft Tersimpan",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF539DF3)
            )
        }
    }
}

@Composable
fun CustomInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean,
    minLines: Int,
    placeholder: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1D2436))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7E8A9F),
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = singleLine,
                    minLines = minLines,
                    placeholder = {
                        Text(
                            text = placeholder,
                            color = Color(0xFF515D72),
                            fontSize = 14.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFF539DF3)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Bonus2Preview() {
    Bonus2()
}
