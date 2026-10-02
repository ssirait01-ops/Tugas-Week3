package com.bruce.exercise3.view

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold

@Composable
fun bonus1() {
    var selectedSize by remember {
        mutableStateOf("Regular (+0)")
    }
    var quantity by remember { mutableStateOf(1) }
    val unitPrice = if (selectedSize == "Large (+6k)") 31000 else 25000
    val subtotal = unitPrice * quantity
    val tax = subtotal / 10
    val total = subtotal + tax
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color(0xFFFBF8F6)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFBF8F6))
                .padding(25.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Kopi Kenangan Senja",
                    fontSize = 18.sp,
                    fontWeight = Bold,
                    color = Color.Black
                )

                Text(
                    text = "☕",
                    fontSize = 32.sp
                )
            }
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, Color(0xFFF5E1C0)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF633A1E),
                                        Color(0xFF853F22)
                                    )
                                )
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Coffee,
                            contentDescription = "Coffee",
                            tint = Color(0xFFF0D9B5),
                            modifier = Modifier.size(80.dp)
                        )

                    }
                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = "Caramel latte",
                        fontSize = 24.sp,
                        fontWeight = Bold,
                        color = Color(0xFF853F22)
                    )

                    Text(
                        text = "Espresso shot, steamed milk & caramel syrup",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF5A3A2E)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Rp 25.000  / cup",
                        fontSize = 18.sp,
                        fontWeight = ExtraBold,
                        color = Color(0xFFC8703A)
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Pilihan Ukuran:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                for (size in listOf("Regular (+0)", "Large (+6k)")) {
                    val isSelected = selectedSize == size
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .clip(RoundedCornerShape(2.dp))
                            .background((if (isSelected) Color.White else Color(0xFFFEF6EC)))
                            .border(
                                width = 2.dp,
                                color = if (isSelected) Color(0xFFC8703A) else Color(0xFFF5E1C0),
                                shape = RoundedCornerShape(5.dp)
                            )
                            .clickable { selectedSize = size }
                            .padding(5.dp)
                    ) {
                        Text(
                            text = size,
                            fontWeight = if (isSelected) Bold else FontWeight.Medium,
                            color = if (isSelected) Color(0xFFC8703A) else Color(0xFF5A3A2E)

                        )
                    }
                }
            }
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(2.dp, Color(0xFFF5E1C0)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Jumlah Pesanan:",
                        fontWeight = Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF3B1E14)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(2.dp, Color(0xFFD9DEE8), RoundedCornerShape(10.dp))
                                .clickable { if (quantity > 1) quantity-- }
                        ) {
                            Text(
                                "-",
                                fontWeight = Bold,
                                fontSize = 20.sp,
                                color = Color(0xFF853F22)
                            )
                        }

                        Text(
                            text = quantity.toString(),
                            fontWeight = Bold,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )


                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFCEBD8))
                                .border(2.dp, Color(0xFFE5D2B8), RoundedCornerShape(10.dp))
                                .clickable { quantity++ }
                        ) {
                            Text(
                                "+",
                                fontWeight = Bold,
                                fontSize = 20.sp,
                                color = Color(0xFFC8703A)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFEF6EC))
                    .drawBehind {
                        drawRoundRect(
                            color = Color(0xFFF0C9A0),
                            cornerRadius = CornerRadius(14.dp.toPx()),
                            style = Stroke(
                                width = 2.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f))
                            )
                        )
                    }
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Subtotal:", color = Color(0xFF5A3A2E))
                    Text(formatRp(subtotal), color = Color(0xFF5A3A2E))
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pajak Resto (10%):", color = Color(0xFF5A3A2E))
                    Text(formatRp(tax), color = Color(0xFF5A3A2E))
                }

                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFF0C9A0))
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Total Tagihan:", fontWeight = Bold, fontSize = 18.sp)
                    Text(
                        formatRp(total),
                        fontWeight = Bold,
                        fontSize = 18.sp,
                        color = Color(0xFFC8703A)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "$quantity x Caramel Latte ($selectedSize) ditambahkan ke keranjang"
                        )
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC8703A)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "TAMBAH KE KERANJANG",
                    fontWeight = Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}

fun formatRp(amount: Int): String {
    return "Rp " + "%,d".format(amount).replace(',', '.')
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun bonus1Preview() {
    bonus1()
}