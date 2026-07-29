package com.example.luminarcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.ui.CalculatorScreen
import com.example.luminarcalculator.ui.CalculatorViewModel
import com.example.luminarcalculator.ui.EngineeringScreen
import com.example.luminarcalculator.ui.FormulaLibraryScreen
import com.example.luminarcalculator.ui.AIAssistantSheet
import com.example.luminarcalculator.ui.SplashVideoScreen
import com.example.luminarcalculator.ui.components.AnimatedThemeToggle
import com.example.luminarcalculator.ui.components.ExecutiveInfoDialog
import com.example.luminarcalculator.ui.theme.LuminarCalculatorTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showSplash by remember { mutableStateOf(true) }
            var currentScreen by remember { mutableStateOf("calculator") } // "calculator", "engineering", "formulas"
            var isDarkMode by remember { mutableStateOf(true) }
            var showInfoDialog by remember { mutableStateOf(false) }
            var showAiAssistant by remember { mutableStateOf(false) }

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

                                        // Executive Top Control Bar
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .statusBarsPadding()
                                                .padding(horizontal = 20.dp, vertical = 12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            AnimatedThemeToggle(
                                                isDarkMode = isDarkMode,
                                                onToggle = { isDarkMode = !isDarkMode }
                                            )

                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                // AI Assistant trigger button
                                                Box(
                                                    modifier = Modifier
                                                        .size(38.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFF1E293B).copy(alpha = 0.8f))
                                                        .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f), CircleShape)
                                                        .clickable { showAiAssistant = true },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.AutoAwesome,
                                                        contentDescription = "AI Assistant",
                                                        tint = Color(0xFF38BDF8),
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }

                                                // Info Button
                                                Box(
                                                    modifier = Modifier
                                                        .size(38.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFF1E293B).copy(alpha = 0.8f))
                                                        .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f), CircleShape)
                                                        .clickable { showInfoDialog = true },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = "i",
                                                        color = Color(0xFF38BDF8),
                                                        fontSize = 16.sp,
                                                        fontFamily = FontFamily.Serif,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }

                                                // Engineering Module Button
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(14.dp))
                                                        .background(Color(0xFF0F172A).copy(alpha = 0.9f))
                                                        .border(1.dp, Color(0xFF6366F1).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                                        .clickable { currentScreen = "engineering" }
                                                        .padding(horizontal = 14.dp, vertical = 8.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = "ENGINEERING",
                                                        color = Color(0xFF818CF8),
                                                        fontSize = 11.sp,
                                                        fontFamily = FontFamily.Monospace,
                                                        fontWeight = FontWeight.Bold,
                                                        letterSpacing = 1.5.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                "engineering" -> {
                                    EngineeringScreen(
                                        onBack = { currentScreen = "calculator" },
                                        onNavigateToFormulas = { currentScreen = "formulas" }
                                    )
                                }
                                "formulas" -> {
                                    FormulaLibraryScreen(
                                        onBack = { currentScreen = "engineering" }
                                    )
                                }
                            }

                            if (showInfoDialog) {
                                ExecutiveInfoDialog(
                                    onDismiss = { showInfoDialog = false }
                                )
                            }

                            if (showAiAssistant) {
                                AIAssistantSheet(
                                    onDismiss = { showAiAssistant = false }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
