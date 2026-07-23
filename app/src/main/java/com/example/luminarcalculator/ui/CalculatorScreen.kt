package com.example.luminarcalculator.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.ui.components.ButtonRole
import com.example.luminarcalculator.ui.components.NeumorphicButton
import com.example.luminarcalculator.ui.theme.*

@Composable
fun CalculatorScreen() {
    var isDarkMode by remember { mutableStateOf(true) }
    var expression by remember { mutableStateOf("6000/2:+3227:2") }
    var result by remember { mutableStateOf("12,454") }

    // Smooth background theme transition
    val animatedBgColor by animateColorAsState(
        targetValue = if (isDarkMode) DarkBackground else LightBackground,
        animationSpec = tween(durationMillis = 300), label = "ThemeTransition"
    )

    val textColorPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textColorSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBgColor)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- 1. Top Theme Switcher & Display ---
        Column(modifier = Modifier.fillMaxWidth()) {
            // Light / Dark Mode Toggle Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isDarkMode) Color(0xFF1A202C) else Color(0xFFD6E3F2))
                        .clickable { isDarkMode = !isDarkMode }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (isDarkMode) "🌙  Dark Mode" else "☀️  Light Mode",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = textColorSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Expression & Main Result
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = expression,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                    color = textColorSecondary,
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "=$result",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColorPrimary,
                    textAlign = TextAlign.End
                )
            }
        }

        // --- 2. Neumorphic Keypad Grid (Exact Match to Reference Image) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Row 1: Scientific Top Utility Row
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("sc", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) {}
                NeumorphicButton("sin", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) {}
                NeumorphicButton("deg", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) {}
                NeumorphicButton("%", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) {}
            }

            // Row 2
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("Ac", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) {
                    expression = ""
                    result = "0"
                }
                NeumorphicButton("8", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) {}
                NeumorphicButton("9", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) {}
                NeumorphicButton("÷", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) {}
            }

            // Row 3
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("4", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) {}
                NeumorphicButton("5", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) {}
                NeumorphicButton("6", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) {}
                NeumorphicButton("×", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) {}
            }

            // Row 4 & 5 Combined with Tall Accent Equals Key
            Row(modifier = Modifier.fillMaxWidth()) {
                // Left 3 columns
                Column(modifier = Modifier.weight(3f)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        NeumorphicButton("1", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) {}
                        NeumorphicButton("2", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) {}
                        NeumorphicButton("3", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) {}
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        NeumorphicButton("0", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) {}
                        NeumorphicButton("•", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) {}
                        NeumorphicButton("+", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) {}
                    }
                }

                // Right column: Tall Electric Blue Equals Button
                Column(modifier = Modifier.weight(1f)) {
                    NeumorphicButton(
                        symbol = "=",
                        role = ButtonRole.ACCENT_EQUALS,
                        isDarkMode = isDarkMode,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(138.dp) // Spans two row heights perfectly
                    ) {}
                }
            }
        }
    }
}
