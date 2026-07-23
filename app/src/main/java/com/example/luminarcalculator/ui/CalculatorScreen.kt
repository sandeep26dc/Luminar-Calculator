package com.example.luminarcalculator.ui

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.CalculatorEngine
import com.example.luminarcalculator.ui.components.ButtonRole
import com.example.luminarcalculator.ui.components.NeumorphicButton
import com.example.luminarcalculator.ui.theme.*

@Composable
fun CalculatorScreen() {
    var isDarkMode by rememberSaveable { mutableStateOf(true) }
    var currentTab by rememberSaveable { mutableIntStateOf(0) }
    var expression by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("0") }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val animatedBgColor by animateColorAsState(
        targetValue = if (isDarkMode) DarkBackground else LightBackground,
        animationSpec = tween(durationMillis = 300), label = "Theme"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBgColor)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isDarkMode) Color(0xFF14181F) else Color(0xFFD6E3F2))
                    .padding(4.dp)
            ) {
                TabChip("Calc", currentTab == 0, isDarkMode) { currentTab = 0 }
                TabChip("Graph", currentTab == 1, isDarkMode) { currentTab = 1 }
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isDarkMode) Color(0xFF14181F) else Color(0xFFD6E3F2))
                    .clickable { isDarkMode = !isDarkMode }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(text = if (isDarkMode) "🌙" else "☀️", fontSize = 14.sp)
            }
        }

        if (currentTab == 1) {
            GraphScreen(isDarkMode = isDarkMode)
        } else {
            if (isLandscape) {
                LandscapeCalculatorLayout(
                    isDarkMode = isDarkMode,
                    expression = expression,
                    result = result,
                    onKeyClick = { key ->
                        handleInput(key, expression, result, { expression = it }, { result = it })
                    }
                )
            } else {
                PortraitCalculatorLayout(
                    isDarkMode = isDarkMode,
                    expression = expression,
                    result = result,
                    onKeyClick = { key ->
                        handleInput(key, expression, result, { expression = it }, { result = it })
                    }
                )
            }
        }
    }
}

@Composable
private fun TabChip(label: String, isSelected: Boolean, isDarkMode: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) (if (isDarkMode) DarkKeyAccent else LightKeyAccent)
                else Color.Transparent
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else (if (isDarkMode) DarkTextSecondary else LightTextSecondary)
        )
    }
}

private fun handleInput(
    key: String,
    currentExpr: String,
    currentRes: String,
    setExpr: (String) -> Unit,
    setRes: (String) -> Unit
) {
    when (key) {
        "Ac", "AC" -> {
            setExpr("")
            setRes("0")
        }
        "=" -> {
            if (currentExpr.isNotEmpty()) {
                val eval = CalculatorEngine.evaluate(currentExpr)
                setRes(eval)
            }
        }
        "deg", "rad" -> {
            CalculatorEngine.isDegreeMode = !CalculatorEngine.isDegreeMode
        }
        else -> {
            setExpr(currentExpr + key)
        }
    }
}

@Composable
private fun PortraitCalculatorLayout(
    isDarkMode: Boolean,
    expression: String,
    result: String,
    onKeyClick: (String) -> Unit
) {
    val textColorPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textColorSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(text = expression, fontSize = 24.sp, color = textColorSecondary, textAlign = TextAlign.End)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "=$result", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = textColorPrimary, textAlign = TextAlign.End)
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("sin", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("sin(") }
                NeumorphicButton("cos", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("cos(") }
                NeumorphicButton("deg", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("deg") }
                NeumorphicButton("%", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("%") }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("Ac", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("Ac") }
                NeumorphicButton("8", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("8") }
                NeumorphicButton("9", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("9") }
                NeumorphicButton("÷", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("÷") }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("4", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("4") }
                NeumorphicButton("5", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("5") }
                NeumorphicButton("6", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("6") }
                NeumorphicButton("×", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("×") }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(3f)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        NeumorphicButton("1", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("1") }
                        NeumorphicButton("2", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("2") }
                        NeumorphicButton("3", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("3") }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        NeumorphicButton("0", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("0") }
                        NeumorphicButton("•", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("•") }
                        NeumorphicButton("+", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("+") }
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    NeumorphicButton("=", ButtonRole.ACCENT_EQUALS, isDarkMode, modifier = Modifier.fillMaxWidth().height(138.dp)) { onKeyClick("=") }
                }
            }
        }
    }
}

@Composable
private fun LandscapeCalculatorLayout(
    isDarkMode: Boolean,
    expression: String,
    result: String,
    onKeyClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(text = expression, fontSize = 20.sp, color = if (isDarkMode) DarkTextSecondary else LightTextSecondary)
            Text(text = "=$result", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = if (isDarkMode) DarkTextPrimary else LightTextPrimary)
        }

        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("sin", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("sin(") }
                NeumorphicButton("cos", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("cos(") }
                NeumorphicButton("tan", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("tan(") }
                NeumorphicButton("log", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("log(") }
                NeumorphicButton("Ac", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("Ac") }
                NeumorphicButton("÷", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("÷") }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("7", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("7") }
                NeumorphicButton("8", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("8") }
                NeumorphicButton("9", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("9") }
                NeumorphicButton("×", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("×") }
                NeumorphicButton("-", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("-") }
                NeumorphicButton("+", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("+") }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("4", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("4") }
                NeumorphicButton("5", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("5") }
                NeumorphicButton("6", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("6") }
                NeumorphicButton("1", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("1") }
                NeumorphicButton("2", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("2") }
                NeumorphicButton("3", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("3") }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("0", ButtonRole.NUMBER, isDarkMode, Modifier.weight(2f)) { onKeyClick("0") }
                NeumorphicButton("•", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("•") }
                NeumorphicButton("=", ButtonRole.ACCENT_EQUALS, isDarkMode, Modifier.weight(3f)) { onKeyClick("=") }
            }
        }
    }
}
