package com.example.luminarcalculator.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.AngleMode
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
    var isScientificOpen by remember { mutableStateOf(false) }
    var currentAngleMode by remember { mutableStateOf(AngleMode.DEG) }

    val engine = remember { CalculatorEngine() }

    fun onAction(action: String) {
        when (action) {
            "AC" -> {
                expression = ""
                result = "0"
            }
            "⌫" -> {
                if (expression.isNotEmpty()) {
                    expression = expression.dropLast(1)
                    result = if (expression.isEmpty()) "0" else engine.evaluate(expression)
                }
            }
            "=" -> {
                val finalEval = engine.evaluate(expression)
                if (finalEval != "Error") {
                    result = finalEval
                }
            }
            "DEG/RAD" -> {
                currentAngleMode = if (currentAngleMode == AngleMode.DEG) AngleMode.RAD else AngleMode.DEG
                engine.angleMode = currentAngleMode
                if (expression.isNotEmpty()) result = engine.evaluate(expression)
            }
            "MC" -> engine.memoryClear()
            "MR" -> {
                expression += engine.memoryRecall()
                result = engine.evaluate(expression)
            }
            "M+" -> engine.memoryAdd(result)
            "M-" -> engine.memorySubtract(result)
            else -> {
                expression += action
                val eval = engine.evaluate(expression)
                if (eval != "Error") result = eval
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
    ) {
        // --- TOP TOOLBAR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { isScientificOpen = !isScientificOpen },
                colors = ButtonDefaults.buttonColors(containerColor = if (isDark) Color(0xFF1E2638) else Color(0xFFDCE5F0))
            ) {
                Text(
                    text = if (isScientificOpen) "Standard" else "Scientific (Fx)",
                    color = ElectricBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "MODE: ${currentAngleMode.name}",
                color = secondaryTextColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // --- DISPLAY SECTION ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = if (expression.isEmpty()) "0" else expression,
                color = secondaryTextColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "= $result",
                color = primaryTextColor,
                fontSize = if (result.length > 10) 32.sp else 44.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }

        // --- MEMORY BAR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalculatorButton("MC", ButtonType.FUNCTION, Modifier.weight(1f)) { onAction("MC") }
            CalculatorButton("MR", ButtonType.FUNCTION, Modifier.weight(1f)) { onAction("MR") }
            CalculatorButton("M+", ButtonType.FUNCTION, Modifier.weight(1f)) { onAction("M+") }
            CalculatorButton("M-", ButtonType.FUNCTION, Modifier.weight(1f)) { onAction("M-") }
        }

        // --- OPTIONAL SCIENTIFIC DRAWER ---
        AnimatedVisibility(
            visible = isScientificOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    CalculatorButton(currentAngleMode.name, ButtonType.SCIENTIFIC, Modifier.weight(1f)) { onAction("DEG/RAD") }
                    CalculatorButton("sin", ButtonType.SCIENTIFIC, Modifier.weight(1f)) { onAction("sin(") }
                    CalculatorButton("cos", ButtonType.SCIENTIFIC, Modifier.weight(1f)) { onAction("cos(") }
                    CalculatorButton("tan", ButtonType.SCIENTIFIC, Modifier.weight(1f)) { onAction("tan(") }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    CalculatorButton("ln", ButtonType.SCIENTIFIC, Modifier.weight(1f)) { onAction("ln(") }
                    CalculatorButton("log", ButtonType.SCIENTIFIC, Modifier.weight(1f)) { onAction("log(") }
                    CalculatorButton("√", ButtonType.SCIENTIFIC, Modifier.weight(1f)) { onAction("√(") }
                    CalculatorButton("^", ButtonType.SCIENTIFIC, Modifier.weight(1f)) { onAction("^") }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    CalculatorButton("π", ButtonType.SCIENTIFIC, Modifier.weight(1f)) { onAction("π") }
                    CalculatorButton("e", ButtonType.SCIENTIFIC, Modifier.weight(1f)) { onAction("e") }
                    CalculatorButton("(", ButtonType.SCIENTIFIC, Modifier.weight(1f)) { onAction("(") }
                    CalculatorButton(")", ButtonType.SCIENTIFIC, Modifier.weight(1f)) { onAction(")") }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // --- STANDARD KEYPAD GRID ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Row 1
            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("AC", ButtonType.FUNCTION, Modifier.weight(1f)) { onAction("AC") }
                CalculatorButton("⌫", ButtonType.FUNCTION, Modifier.weight(1f)) { onAction("⌫") }
                CalculatorButton("%", ButtonType.FUNCTION, Modifier.weight(1f)) { onAction("%") }
                CalculatorButton("÷", ButtonType.OPERATOR, Modifier.weight(1f)) { onAction("÷") }
            }

            // Row 2
            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("7", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("7") }
                CalculatorButton("8", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("8") }
                CalculatorButton("9", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("9") }
                CalculatorButton("×", ButtonType.OPERATOR, Modifier.weight(1f)) { onAction("×") }
            }

            // Row 3
            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("4", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("4") }
                CalculatorButton("5", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("5") }
                CalculatorButton("6", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("6") }
                CalculatorButton("-", ButtonType.OPERATOR, Modifier.weight(1f)) { onAction("-") }
            }

            // Row 4
            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("1", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("1") }
                CalculatorButton("2", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("2") }
                CalculatorButton("3", ButtonType.STANDARD, Modifier.weight(1f)) { onAction("3") }
                CalculatorButton("+", ButtonType.OPERATOR, Modifier.weight(1f)) { onAction("+") }
            }

            // Row 5
            Row(modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("0", ButtonType.STANDARD, Modifier.weight(2f)) { onAction("0") }
                CalculatorButton(".", ButtonType.STANDARD, Modifier.weight(1f)) { onAction(".") }
                CalculatorButton("=", ButtonType.ACCENT, Modifier.weight(1f)) { onAction("=") }
            }
        }
    }
}
