package com.example.luminarcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.luminarcalculator.ui.CalculatorScreen
import com.example.luminarcalculator.ui.CalculatorViewModel
import com.example.luminarcalculator.ui.EngineeringScreen
import com.example.luminarcalculator.ui.SplashVideoScreen
import com.example.luminarcalculator.ui.components.AnimatedThemeToggle
import com.example.luminarcalculator.ui.components.ExecutiveInfoDialog
import com.example.luminarcalculator.ui.theme.LuminarCalculatorTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showSplash by remember { mutableStateOf(true) }
            var currentScreen by remember { mutableStateOf("calculator") }
            var isDarkMode by remember { mutableStateOf(true) }
            var showInfoDialog by remember { mutableStateOf(false) }

            LuminarCalculatorTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showSplash) {
                        SplashVideoScreen(
                            onSplashFinished = { showSplash = false }
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize()) {
                            when (currentScreen) {
                                "calculator" -> {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        CalculatorScreen(
                                            displayValue = viewModel.calculationResult.ifEmpty { viewModel.currentExpression.ifEmpty { "0" } },
                                            expressionValue = viewModel.currentExpression,
                                            onButtonClick = { symbol -> viewModel.onAction(symbol) }
                                        )

                                        // Top control bar icons (Theme Toggle, Info Dialog, and Engineering Switcher)
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .statusBarsPadding()
                                                .padding(16.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            AnimatedThemeToggle(
                                                isDarkMode = isDarkMode,
                                                onToggle = { isDarkMode = !isDarkMode }
                                            )

                                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                IconButton(onClick = { showInfoDialog = true }) {
                                                    Text(text = "ℹ️", fontSize = 20.sp)
                                                }
                                                Button(
                                                    onClick = { currentScreen = "engineering" },
                                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                                ) {
                                                    Text("Engineering")
                                                }
                                            }
                                        }
                                    }
                                }
                                "engineering" -> {
                                    EngineeringScreen(
                                        onBack = { currentScreen = "calculator" }
                                    )
                                }
                            }

                            if (showInfoDialog) {
                                ExecutiveInfoDialog(
                                    onDismiss = { showInfoDialog = false }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
