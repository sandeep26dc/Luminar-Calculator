package com.example.luminarcalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.ui.components.LuminarButton
import com.example.luminarcalculator.ui.components.LuminarButtonType
import com.example.luminarcalculator.ui.theme.*

@Composable
fun CalculatorScreen() {
    var expression by remember { mutableStateOf("100×25") }
    var result by remember { mutableStateOf("2,500") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- 1. Glass Display Window ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 16.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(28.dp),
                    spotColor = Color(0x3300E5FF)
                )
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(SurfaceGlassTop, SurfaceGlassBottom)
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(GlassBorder, Color.Transparent)
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(24.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = expression,
                    fontSize = 28.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "= $result",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.End
                )
            }
        }

        // --- 2. Keypad Grid ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Row 1: Memory & Clear
            Row(modifier = Modifier.fillMaxWidth()) {
                LuminarButton("MC", LuminarButtonType.ACTION, Modifier.weight(1f)) {}
                LuminarButton("MR", LuminarButtonType.ACTION, Modifier.weight(1f)) {}
                LuminarButton("M+", LuminarButtonType.ACTION, Modifier.weight(1f)) {}
                LuminarButton("M-", LuminarButtonType.ACTION, Modifier.weight(1f)) {}
            }
            // Row 2
            Row(modifier = Modifier.fillMaxWidth()) {
                LuminarButton("AC", LuminarButtonType.ACTION, Modifier.weight(1f)) {
                    expression = ""
                    result = "0"
                }
                LuminarButton("⌫", LuminarButtonType.ACTION, Modifier.weight(1f)) {}
                LuminarButton("%", LuminarButtonType.ACTION, Modifier.weight(1f)) {}
                LuminarButton("÷", LuminarButtonType.OPERATOR, Modifier.weight(1f)) {}
            }
            // Row 3
            Row(modifier = Modifier.fillMaxWidth()) {
                LuminarButton("7", LuminarButtonType.NUMBER, Modifier.weight(1f)) {}
                LuminarButton("8", LuminarButtonType.NUMBER, Modifier.weight(1f)) {}
                LuminarButton("9", LuminarButtonType.NUMBER, Modifier.weight(1f)) {}
                LuminarButton("×", LuminarButtonType.OPERATOR, Modifier.weight(1f)) {}
            }
            // Row 4
            Row(modifier = Modifier.fillMaxWidth()) {
                LuminarButton("4", LuminarButtonType.NUMBER, Modifier.weight(1f)) {}
                LuminarButton("5", LuminarButtonType.NUMBER, Modifier.weight(1f)) {}
                LuminarButton("6", LuminarButtonType.NUMBER, Modifier.weight(1f)) {}
                LuminarButton("-", LuminarButtonType.OPERATOR, Modifier.weight(1f)) {}
            }
            // Row 5
            Row(modifier = Modifier.fillMaxWidth()) {
                LuminarButton("1", LuminarButtonType.NUMBER, Modifier.weight(1f)) {}
                LuminarButton("2", LuminarButtonType.NUMBER, Modifier.weight(1f)) {}
                LuminarButton("3", LuminarButtonType.NUMBER, Modifier.weight(1f)) {}
                LuminarButton("+", LuminarButtonType.OPERATOR, Modifier.weight(1f)) {}
            }
            // Row 6
            Row(modifier = Modifier.fillMaxWidth()) {
                LuminarButton("0", LuminarButtonType.NUMBER, Modifier.weight(2f)) {}
                LuminarButton(".", LuminarButtonType.NUMBER, Modifier.weight(1f)) {}
                LuminarButton("=", LuminarButtonType.EQUALS, Modifier.weight(1f)) {}
            }
        }
    }
}
