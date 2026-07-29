package com.example.luminarcalculator.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Standard : Screen("standard", "Calc", Icons.Default.Calculate)
    object Formulas : Screen("formulas", "Formulas", Icons.Default.Code)
    object Units : Screen("units", "Units", Icons.Default.SwapHoriz)
    object Currency : Screen("currency", "Currency", Icons.Default.AttachMoney)
    object UnitRates : Screen("unit_rates", "Rates", Icons.Default.Assessment)
    object Constants : Screen("constants", "Handbook", Icons.Default.Book)
    object Estimator : Screen("estimator", "Estimator", Icons.Default.Build)
}

@Composable
fun MainNavigation(isDarkMode: Boolean) {
    var currentScreen by rememberSaveable { mutableStateOf<Screen>(Screen.Standard) }
    var showAiSheet by rememberSaveable { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val calcViewModel: CalculatorViewModel = viewModel()

    val screens = listOf(
        Screen.Standard,
        Screen.Formulas,
        Screen.Units,
        Screen.Currency,
        Screen.UnitRates,
        Screen.Constants,
        Screen.Estimator
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 82.dp)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    (fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) +
                            scaleIn(
                                initialScale = 0.94f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                            )).togetherWith(
                        fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))
                    )
                },
                label = "mercury_screen_transition"
            ) { screen ->
                when (screen) {
                    is Screen.Standard -> {
                        CalculatorScreen(
                            displayValue = calcViewModel.calculationResult.ifEmpty { calcViewModel.currentExpression.ifEmpty { "0" } },
                            expressionValue = calcViewModel.currentExpression,
                            onButtonClick = { action -> calcViewModel.onAction(action) }
                        )
                    }
                    is Screen.Formulas -> {
                        FormulaLibraryScreen(
                            viewModel = calcViewModel,
                            onBack = { currentScreen = Screen.Standard }
                        )
                    }
                    is Screen.Units -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Unit Converter Active", color = Color(0xFF94A3B8))
                        }
                    }
                    is Screen.Currency -> {
                        CurrencyScreen()
                    }
                    is Screen.UnitRates -> {
                        UnitRateScreen()
                    }
                    is Screen.Constants -> {
                        ConstantsScreen()
                    }
                    is Screen.Estimator -> {
                        EstimatorScreen()
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 98.dp)
        ) {
            AnimatedExecutiveFAB {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                showAiSheet = true
            }
        }

        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = Color(0xFF0F172A).copy(alpha = 0.80f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(82.dp)
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFF38BDF8).copy(alpha = 0.4f),
                            Color(0xFF6366F1).copy(alpha = 0.15f),
                            Color(0xFF38BDF8).copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                screens.forEach { screen ->
                    val selected = currentScreen == screen

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                if (currentScreen != screen) {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    currentScreen = screen
                                }
                            }
                            .padding(vertical = 6.dp, horizontal = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = if (selected) 38.dp else 26.dp, height = 26.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    brush = if (selected) {
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFF38BDF8).copy(alpha = 0.25f),
                                                Color(0xFF0EA5E9).copy(alpha = 0.15f)
                                            )
                                        )
                                    } else {
                                        Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
                                    },
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .border(
                                    width = if (selected) 1.dp else 0.dp,
                                    color = if (selected) Color(0xFF38BDF8).copy(alpha = 0.5f) else Color.Transparent,
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                tint = if (selected) Color(0xFF38BDF8) else Color(0xFF64748B),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = screen.title,
                            color = if (selected) Color(0xFF38BDF8) else Color(0xFF64748B),
                            fontSize = 8.5.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        if (showAiSheet) {
            AIAssistantSheet(
                onDismiss = { showAiSheet = false }
            )
        }
    }
}
