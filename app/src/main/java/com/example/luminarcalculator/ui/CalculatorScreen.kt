package com.example.luminarcalculator.ui

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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

data class HistoryItem(val expression: String, val result: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen() {
    var isDarkMode by rememberSaveable { mutableStateOf(true) }
    var currentTab by rememberSaveable { mutableIntStateOf(0) } // 0: Calc, 1: Graph, 2: Convert
    var expression by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("0") }
    var livePreview by rememberSaveable { mutableStateOf("") }
    var isScientificExpanded by rememberSaveable { mutableStateOf(false) }
    var showHistorySheet by rememberSaveable { mutableStateOf(false) }
    var showInfoSheet by rememberSaveable { mutableStateOf(false) }

    val historyList = remember { mutableStateListOf<HistoryItem>() }
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
        // --- Header Navigation & Controls ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Action Icons (History & Info Modal)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { showHistorySheet = true }) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History",
                        tint = if (isDarkMode) DarkTextPrimary else LightTextPrimary
                    )
                }
                
                IconButton(onClick = { showInfoSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "App Details",
                        tint = Color(0xFF00E5FF)
                    )
                }
            }

            // Central Navigation Tabs Switcher
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isDarkMode) Color(0xFF14181F) else Color(0xFFD6E3F2))
                    .padding(4.dp)
            ) {
                TabChip("Calc", currentTab == 0, isDarkMode) { currentTab = 0 }
                TabChip("Graph", currentTab == 1, isDarkMode) { currentTab = 1 }
                TabChip("Convert", currentTab == 2, isDarkMode) { currentTab = 2 }
            }

            // Theme Toggle Button
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isDarkMode) Color(0xFF14181F) else Color(0xFFD6E3F2))
                    .clickable { isDarkMode = !isDarkMode }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(text = if (isDarkMode) "🌙" else "☀️", fontSize = 14.sp)
            }
        }

        // --- Screen Views ---
        when (currentTab) {
            1 -> GraphScreen(isDarkMode = isDarkMode)
            2 -> UnitConverterScreen(isDarkMode = isDarkMode)
            else -> {
                if (isLandscape) {
                    LandscapeCalculatorLayout(
                        isDarkMode = isDarkMode,
                        expression = expression,
                        result = result,
                        livePreview = livePreview,
                        onKeyClick = { key ->
                            handleInput(key, expression, result, { expression = it }, { result = it }, { livePreview = it }, historyList)
                        }
                    )
                } else {
                    PortraitCalculatorLayout(
                        isDarkMode = isDarkMode,
                        expression = expression,
                        result = result,
                        livePreview = livePreview,
                        isScientificExpanded = isScientificExpanded,
                        onToggleScientific = { isScientificExpanded = !isScientificExpanded },
                        onKeyClick = { key ->
                            handleInput(key, expression, result, { expression = it }, { result = it }, { livePreview = it }, historyList)
                        }
                    )
                }
            }
        }
    }

    // --- App Info Modal (Floating "i" Details Sheet) ---
    if (showInfoSheet) {
        ModalBottomSheet(
            onDismissRequest = { showInfoSheet = false },
            containerColor = if (isDarkMode) Color(0xFF14181F) else Color(0xFFE3EDF7)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "LUMINAR CALCULATOR",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00E5FF)
                )
                Text(
                    text = "Version 1.0.0 (Enterprise Suite)",
                    fontSize = 12.sp,
                    color = if (isDarkMode) DarkTextSecondary else LightTextSecondary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Divider(color = (if (isDarkMode) DarkTextSecondary else LightTextSecondary).copy(alpha = 0.2f))

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Creator & Lead Engineer",
                    fontSize = 13.sp,
                    color = if (isDarkMode) DarkTextSecondary else LightTextSecondary
                )
                Text(
                    text = "Sandeep Som",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) DarkTextPrimary else LightTextPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Engineered with Jetpack Compose & Neumorphic Physics engine. Built for real-time mathematical operations, interactive function graphing, and high-precision calculations.",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = if (isDarkMode) DarkTextSecondary else LightTextSecondary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // --- History Bottom Sheet ---
    if (showHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showHistorySheet = false },
            containerColor = if (isDarkMode) Color(0xFF14181F) else Color(0xFFE3EDF7)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Calculation History",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) DarkTextPrimary else LightTextPrimary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (historyList.isEmpty()) {
                    Text(
                        text = "No past calculations yet.",
                        fontSize = 14.sp,
                        color = if (isDarkMode) DarkTextSecondary else LightTextSecondary,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxHeight(0.5f)
                    ) {
                        items(historyList.reversed()) { item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isDarkMode) Color(0xFF1C222D) else Color(0xFFD0DFEE))
                                    .clickable {
                                        expression = item.expression
                                        result = item.result
                                        showHistorySheet = false
                                    }
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(text = item.expression, fontSize = 16.sp, color = if (isDarkMode) DarkTextSecondary else LightTextSecondary)
                                Text(text = "=${item.result}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                            }
                        }
                    }
                }
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
            .padding(horizontal = 12.dp, vertical = 6.dp)
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
    setRes: (String) -> Unit,
    setLivePreview: (String) -> Unit,
    historyList: androidx.compose.runtime.snapshots.SnapshotStateList<HistoryItem>
) {
    when (key) {
        "Ac", "AC" -> {
            setExpr("")
            setRes("0")
            setLivePreview("")
        }
        "⌫" -> {
            if (currentExpr.isNotEmpty()) {
                val newExpr = currentExpr.dropLast(1)
                setExpr(newExpr)
                setLivePreview(CalculatorEngine.evaluateLive(newExpr))
            }
        }
        "=" -> {
            if (currentExpr.isNotEmpty()) {
                val finalEval = CalculatorEngine.evaluate(currentExpr)
                setRes(finalEval)
                if (finalEval != "Error") {
                    historyList.add(HistoryItem(currentExpr, finalEval))
                }
                setLivePreview("")
            }
        }
        "deg", "rad" -> {
            CalculatorEngine.isDegreeMode = !CalculatorEngine.isDegreeMode
        }
        else -> {
            val newExpr = currentExpr + key
            setExpr(newExpr)
            setLivePreview(CalculatorEngine.evaluateLive(newExpr))
        }
    }
}

@Composable
private fun PortraitCalculatorLayout(
    isDarkMode: Boolean,
    expression: String,
    result: String,
    livePreview: String,
    isScientificExpanded: Boolean,
    onToggleScientific: () -> Unit,
    onKeyClick: (String) -> Unit
) {
    val textColorPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textColorSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val previewColor = Color(0xFF00E5FF)

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
            Text(text = expression, fontSize = 28.sp, color = textColorSecondary, textAlign = TextAlign.End)
            if (livePreview.isNotEmpty() && livePreview != result) {
                Text(
                    text = livePreview,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = previewColor.copy(alpha = 0.8f),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "=$result", fontSize = 46.sp, fontWeight = FontWeight.Bold, color = textColorPrimary, textAlign = TextAlign.End)
        }

        IconButton(
            onClick = onToggleScientific,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = if (isScientificExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                contentDescription = "Toggle Scientific Keys",
                tint = textColorSecondary
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            AnimatedVisibility(
                visible = isScientificExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        NeumorphicButton("sin", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("sin(") }
                        NeumorphicButton("cos", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("cos(") }
                        NeumorphicButton("tan", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("tan(") }
                        NeumorphicButton("deg", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("deg") }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        NeumorphicButton("π", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("π") }
                        NeumorphicButton("e", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("e") }
                        NeumorphicButton("log", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("log(") }
                        NeumorphicButton("^", ButtonRole.TOP_ROW, isDarkMode, Modifier.weight(1f)) { onKeyClick("^") }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("Ac", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("Ac") }
                NeumorphicButton("( )", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("(") }
                NeumorphicButton("%", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("%") }
                NeumorphicButton("÷", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("÷") }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("7", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("7") }
                NeumorphicButton("8", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("8") }
                NeumorphicButton("9", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("9") }
                NeumorphicButton("×", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("×") }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("4", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("4") }
                NeumorphicButton("5", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("5") }
                NeumorphicButton("6", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("6") }
                NeumorphicButton("-", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("-") }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("1", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("1") }
                NeumorphicButton("2", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("2") }
                NeumorphicButton("3", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("3") }
                NeumorphicButton("+", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("+") }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                NeumorphicButton("0", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("0") }
                NeumorphicButton("•", ButtonRole.NUMBER, isDarkMode, Modifier.weight(1f)) { onKeyClick("•") }
                NeumorphicButton("⌫", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("⌫") }
                NeumorphicButton("=", ButtonRole.ACCENT_EQUALS, isDarkMode, Modifier.weight(1f)) { onKeyClick("=") }
            }
        }
    }
}

@Composable
private fun LandscapeCalculatorLayout(
    isDarkMode: Boolean,
    expression: String,
    result: String,
    livePreview: String,
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
            if (livePreview.isNotEmpty() && livePreview != result) {
                Text(text = livePreview, fontSize = 16.sp, color = Color(0xFF00E5FF))
            }
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
                NeumorphicButton("⌫", ButtonRole.OPERATOR, isDarkMode, Modifier.weight(1f)) { onKeyClick("⌫") }
                NeumorphicButton("=", ButtonRole.ACCENT_EQUALS, isDarkMode, Modifier.weight(2f)) { onKeyClick("=") }
            }
        }
    }
}
