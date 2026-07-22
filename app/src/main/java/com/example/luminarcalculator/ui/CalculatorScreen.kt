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

    fun onAction(value: String) {
        when (value) {
            "AC" -> {
                expression = ""
                result = "0"
            }
            "⌫" -> {
                if (expression.isNotEmpty()) {
                    expression = expression.drop(1)
                    if (expression.isNotEmpty()) {
                        val evalResult = engine.evaluate(expression)
                        if (evalResult != "Error") result = evalResult
                    } else {
                        result = "0"
                    }
                }
            }
            "=" -> {
                result = engine.evaluate(expression)
            }
            else -> {
                expression += value
                val evalResult = engine.evaluate(expression)
                if (evalResult != "Error") {
                    result = evalResult
                }
            }
        }
    }

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
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "= $result",
                color = primaryTextColor,
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }

        // --- KEYPAD GRID SECTION ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("AC", ButtonType.FUNCTION, Modifier.weight(1f)) { onAction("AC") }
                CalculatorButton("⌫", ButtonType.FUNCTION, Modifier.weight(1f)) { onAction("⌫") }
                CalculatorButton("%", ButtonType.FUNCTION, Modifier.weight(1f)) { onAction("%") }
                CalculatorButton("÷", ButtonType.OPERATOR, Modifier.weight(1f)) { onAction("÷") }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("7", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("7") }
                CalculatorButton("8", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("8") }
                CalculatorButton("9", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("9") }
                CalculatorButton("×", ButtonType.OPERATOR, Modifier.weight(1f)) { onAction("×") }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("4", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("4") }
                CalculatorButton("5", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("5") }
                CalculatorButton("6", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("6") }
                CalculatorButton("-", ButtonType.OPERATOR, Modifier.weight(1f)) { onAction("-") }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("1", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("1") }
                CalculatorButton("2", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("2") }
                CalculatorButton("3", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("3") }
                CalculatorButton("+", ButtonType.OPERATOR, Modifier.weight(1f)) { onAction("+") }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("0", ButtonType.STANDARD, Modifier.weight(2f)) { onAction("0") }
                CalculatorButton(".", ButtonType.STANDARD, Modifier.weight(1f)) { onAction(".") }
                CalculatorButton("=", ButtonType.ACCENT, Modifier.weight(1f)) { onAction("=") }
            }
        }
    }
}
