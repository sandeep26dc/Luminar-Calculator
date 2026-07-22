package com.example.luminarcalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.CalculatorEngine
import com.example.luminarcalculator.ui.components.ButtonType
import com.example.luminarcalculator.ui.components.CalculatorButton
import com.example.luminarcalculator.ui.theme.*

@Composable
fun CalculatorScreen() {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) DarkBackground else LightBackground
    val primaryTextColor = if (isDark) DarkButtonText else Color(0xFF1A202C)
    val secondaryTextColor = if (isDark) DarkSecondaryText else LightSecondaryText

    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }
    val engine = remember { CalculatorEngine() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(20.dp)
    ) {
        // --- DISPLAY SECTION ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = expression,
                color = secondaryTextColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "= $result",
                color = primaryTextColor,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // --- KEYPAD GRID SECTION ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Row 1: Function Bar
            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("AC", ButtonType.FUNCTION, Modifier.weight(1f)) {
                    expression = ""
                    result = "0"
                }
                CalculatorButton("±", ButtonType.FUNCTION, Modifier.weight(1f)) {}
                CalculatorButton("%", ButtonType.FUNCTION, Modifier.weight(1f)) {
                    if (expression.isNotEmpty()) expression += "%"
                }
                CalculatorButton("÷", ButtonType.OPERATOR, Modifier.weight(1f)) {
                    expression += "÷"
                }
            }

            // Row 2
            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("7", ButtonType.STANDARD, Modifier.weight(1f)) { expression += "7"; result = engine.evaluate(expression) }
                CalculatorButton("8", ButtonType.STANDARD, Modifier.weight(1f)) { expression += "8"; result = engine.evaluate(expression) }
                CalculatorButton("9", ButtonType.STANDARD, Modifier.weight(1f)) { expression += "9"; result = engine.evaluate(expression) }
                CalculatorButton("×", ButtonType.OPERATOR, Modifier.weight(1f)) { expression += "×" }
            }

            // Row 3
            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("4", ButtonType.STANDARD, Modifier.weight(1f)) { expression += "4"; result = engine.evaluate(expression) }
                CalculatorButton("5", ButtonType.STANDARD, Modifier.weight(1f)) { expression += "5"; result = engine.evaluate(expression) }
                CalculatorButton("6", ButtonType.STANDARD, Modifier.weight(1f)) { expression += "6"; result = engine.evaluate(expression) }
                CalculatorButton("-", ButtonType.OPERATOR, Modifier.weight(1f)) { expression += "-" }
            }

            // Row 4
            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("1", ButtonType.STANDARD, Modifier.weight(1f)) { expression += "1"; result = engine.evaluate(expression) }
                CalculatorButton("2", ButtonType.STANDARD, Modifier.weight(1f)) { expression += "2"; result = engine.evaluate(expression) }
                CalculatorButton("3", ButtonType.STANDARD, Modifier.weight(1f)) { expression += "3"; result = engine.evaluate(expression) }
                CalculatorButton("+", ButtonType.OPERATOR, Modifier.weight(1f)) { expression += "+" }
            }

            // Row 5
            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("0", ButtonType.STANDARD, Modifier.weight(2f)) { expression += "0"; result = engine.evaluate(expression) }
                CalculatorButton(".", ButtonType.STANDARD, Modifier.weight(1f)) { expression += "." }
                CalculatorButton("=", ButtonType.ACCENT, Modifier.weight(1f)) {
                    result = engine.evaluate(expression)
                }
            }
        }
    }
}
