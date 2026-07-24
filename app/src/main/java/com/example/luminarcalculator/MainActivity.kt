package com.example.luminarcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.CalculatorEngine
import com.example.luminarcalculator.ui.CalculatorScreen
import com.example.luminarcalculator.ui.GraphScreen
import com.example.luminarcalculator.ui.UnitConverterScreen
import com.example.luminarcalculator.ui.components.AnimatedThemeToggle
import com.example.luminarcalculator.ui.components.ExecutiveInfoDialog
import com.example.luminarcalculator.ui.theme.LuminarCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkMode by rememberSaveable { mutableStateOf(true) }

            LuminarCalculatorTheme(darkTheme = isDarkMode) {
                MainAppScreen(
                    isDarkMode = isDarkMode,
                    onToggleTheme = { isDarkMode = !isDarkMode }
                )
            }
        }
    }
}

enum class ScreenTab { CALC, GRAPH, CONVERT }

@Composable
fun MainAppScreen(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    var currentTab by rememberSaveable { mutableStateOf(ScreenTab.CALC) }
    var displayValue by rememberSaveable { mutableStateOf("0") }
    var expressionValue by rememberSaveable { mutableStateOf("") }
    var showInfoDialog by rememberSaveable { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Executive Glass Top Control Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Actions (History & Info Modal Trigger)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) }) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "History",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            showInfoDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "App Release Info",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Center Tab Selector Segment
                    Row(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.background,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        ScreenTab.values().forEach { tab ->
                            val isSelected = currentTab == tab
                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    currentTab = tab
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(10.dp),
                                elevation = ButtonDefaults.buttonElevation(0.dp)
                            ) {
                                Text(
                                    text = tab.name.lowercase().replaceFirstChar { it.uppercase() },
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    // Right Side: Animated Looping Sun/Moon Theme Toggle Component
                    AnimatedThemeToggle(
                        isDarkMode = isDarkMode,
                        onToggle = onToggleTheme
                    )
                }
            }

            // Screen Tab Content Routing
            Box(modifier = Modifier.weight(1f)) {
                when (currentTab) {
                    ScreenTab.CALC -> CalculatorScreen(
                        displayValue = displayValue,
                        expressionValue = expressionValue,
                        onButtonClick = { symbol ->
                            val state = CalculatorEngine.handleInput(symbol, displayValue, expressionValue)
                            displayValue = state.display
                            expressionValue = state.expression
                        }
                    )
                    ScreenTab.GRAPH -> GraphScreen(isDarkMode = isDarkMode)
                    ScreenTab.CONVERT -> UnitConverterScreen(isDarkMode = isDarkMode)
                }
            }
        }
    }

    // Executive Version History Modal Dialog
    if (showInfoDialog) {
        ExecutiveInfoDialog(onDismiss = { showInfoDialog = false })
    }
}
