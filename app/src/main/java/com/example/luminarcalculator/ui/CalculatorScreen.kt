package com.example.luminarcalculator.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.*
import com.example.luminarcalculator.ui.components.ButtonType
import com.example.luminarcalculator.ui.components.CalculatorButton
import com.example.luminarcalculator.ui.theme.*

enum class AppTab { CALCULATOR, CONVERTER, GRAPHING, HISTORY, SETTINGS }

@Composable
fun CalculatorScreen() {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) DarkBackground else LightBackground
    val primaryTextColor = if (isDark) DarkButtonText else Color(0xFF1A202C)
    val secondaryTextColor = if (isDark) DarkSecondaryText else LightSecondaryText

    var selectedTab by remember { mutableStateOf(AppTab.CALCULATOR) }
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }
    var isScientificOpen by remember { mutableStateOf(false) }
    var currentAngleMode by remember { mutableStateOf(AngleMode.DEG) }
    var decimalPrecision by remember { mutableStateOf(4) }

    val historyList = remember { mutableStateListOf<HistoryItem>() }
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
                if (finalEval != "Error" && expression.isNotEmpty()) {
                    result = finalEval
                    historyList.add(0, HistoryItem(expression, finalEval))
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
        // --- NAVIGATION TABS ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            AppTab.values().forEach { tab ->
                Text(
                    text = tab.name.take(4),
                    color = if (selectedTab == tab) ElectricBlue else secondaryTextColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { selectedTab = tab }
                        .padding(6.dp)
                )
            }
        }

        Divider(color = secondaryTextColor.copy(alpha = 0.2f), thickness = 1.dp)

        when (selectedTab) {
            AppTab.CALCULATOR -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 12.dp),
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

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
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

            AppTab.CONVERTER -> ConverterScreen(secondaryTextColor, primaryTextColor)
            AppTab.GRAPHING -> GraphingScreen(secondaryTextColor, primaryTextColor)
            AppTab.HISTORY -> HistoryScreen(historyList, secondaryTextColor, primaryTextColor) { selectedExpr ->
                expression = selectedExpr
                result = engine.evaluate(selectedExpr)
                selectedTab = AppTab.CALCULATOR
            }
            AppTab.SETTINGS -> SettingsScreen(decimalPrecision, secondaryTextColor, primaryTextColor) { newPrec ->
                decimalPrecision = newPrec
            }
        }
    }
}

@Composable
fun GraphingScreen(secondaryTextColor: Color, primaryTextColor: Color) {
    var functionInput by remember { mutableStateOf("sin(x)") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("2D Function Grapher", color = ElectricBlue, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = functionInput,
            onValueChange = { functionInput = it },
            label = { Text("f(x) = ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF0F172A))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val centerX = width / 2
                val centerY = height / 2
                val scale = width / 20f

                // Axes
                drawLine(Color.Gray.copy(alpha = 0.5f), Offset(0f, centerY), Offset(width, centerY), strokeWidth = 2f)
                drawLine(Color.Gray.copy(alpha = 0.5f), Offset(centerX, 0f), Offset(centerX, height), strokeWidth = 2f)

                // Function Plot
                val path = Path()
                var firstPoint = true

                for (pixelX in 0..width.toInt() step 2) {
                    val x = (pixelX - centerX) / scale
                    val y = GraphEvaluator.evaluatePoint(functionInput, x.toDouble())

                    if (y != null) {
                        val pixelY = centerY - (y.toFloat() * scale)
                        if (firstPoint) {
                            path.moveTo(pixelX.toFloat(), pixelY)
                            firstPoint = false
                        } else {
                            path.lineTo(pixelX.toFloat(), pixelY)
                        }
                    }
                }

                drawPath(path, color = ElectricBlue, style = Stroke(width = 4f))
            }
        }
    }
}

@Composable
fun ConverterScreen(secondaryTextColor: Color, primaryTextColor: Color) {
    var inputValue by remember { mutableStateOf("1") }
    var category by remember { mutableStateOf("Length") }
    var fromUnit by remember { mutableStateOf("Meters") }
    var toUnit by remember { mutableStateOf("Feet") }

    val convertedValue = remember(inputValue, category, fromUnit, toUnit) {
        val num = inputValue.toDoubleOrNull() ?: 0.0
        when (category) {
            "Length" -> UnitConverter.convertLength(num, fromUnit, toUnit)
            "Mass" -> UnitConverter.convertMass(num, fromUnit, toUnit)
            "Temperature" -> UnitConverter.convertTemperature(num, fromUnit, toUnit)
            "Area" -> UnitConverter.convertArea(num, fromUnit, toUnit)
            "Data" -> UnitConverter.convertData(num, fromUnit, toUnit)
            else -> 0.0
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Unit Converter", color = ElectricBlue, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text("Input Value") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Category: $category", color = primaryTextColor, fontWeight = FontWeight.Medium)
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Button(onClick = { category = "Length"; fromUnit = "Meters"; toUnit = "Feet" }) { Text("Length", fontSize = 10.sp) }
            Button(onClick = { category = "Mass"; fromUnit = "Kilograms"; toUnit = "Pounds" }) { Text("Mass", fontSize = 10.sp) }
            Button(onClick = { category = "Area"; fromUnit = "Sq Meters"; toUnit = "Sq Feet" }) { Text("Area", fontSize = 10.sp) }
            Button(onClick = { category = "Data"; fromUnit = "MB"; toUnit = "GB" }) { Text("Data", fontSize = 10.sp) }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ElectricBlue.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Result", color = secondaryTextColor, fontSize = 14.sp)
                Text(
                    text = "$inputValue $fromUnit = %.4f $toUnit".format(convertedValue),
                    color = primaryTextColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun HistoryScreen(
    historyList: List<HistoryItem>,
    secondaryTextColor: Color,
    primaryTextColor: Color,
    onSelectHistory: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Calculation History", color = ElectricBlue, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        if (historyList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No calculation history yet", color = secondaryTextColor)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(historyList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectHistory(item.expression) },
                        colors = CardDefaults.cardColors(containerColor = secondaryTextColor.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = item.expression, color = secondaryTextColor, fontSize = 16.sp)
                            Text(text = "= ${item.result}", color = primaryTextColor, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    currentPrecision: Int,
    secondaryTextColor: Color,
    primaryTextColor: Color,
    onPrecisionChange: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings & Preferences", color = ElectricBlue, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        Text("Decimal Precision: $currentPrecision Places", color = primaryTextColor, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(2, 4, 6, 8).forEach { prec ->
                Button(
                    onClick = { onPrecisionChange(prec) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentPrecision == prec) ElectricBlue else Color.Gray.copy(alpha = 0.3f)
                    )
                ) {
                    Text("$prec")
                }
            }
        }
    }
}
