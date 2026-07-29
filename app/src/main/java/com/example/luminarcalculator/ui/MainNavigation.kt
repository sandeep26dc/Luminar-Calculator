package com.example.luminarcalculator.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Standard : Screen("standard", "Calc", Icons.Default.Calculate)
    object Formulas : Screen("formulas", "Formulas", Icons.Default.Code)
    object Units : Screen("units", "Units", Icons.Default.SwapHoriz)
    object Currency : Screen("currency", "Currency", Icons.Default.AttachMoney)
    object Constants : Screen("constants", "Handbook", Icons.Default.Book)
    object Estimator : Screen("estimator", "Estimator", Icons.Default.Build)
}

@Composable
fun MainNavigation(isDarkMode: Boolean) {
    var currentScreen by rememberSaveable { mutableStateOf<Screen>(Screen.Standard) }
    var showAiSheet by rememberSaveable { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val screens = listOf(
        Screen.Standard,
        Screen.Formulas,
        Screen.Units,
        Screen.Currency,
        Screen.Constants,
        Screen.Estimator
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
    ) {
        // Main Screen Content with fluid enter/exit transition
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Leave space for bottom bar
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) +
                            scaleIn(
                                initialScale = 0.96f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            ) togetherWith
                            fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))
                },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    is Screen.Standard -> {
                        // Pass your existing Standard Calculator content or composable here
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Standard Calculator Screen Active", color = Color(0xFF94A3B8))
                        }
                    }
                    is Screen.Formulas -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Custom Formula Library Screen Active", color = Color(0xFF94A3B8))
                        }
                    }
                    is Screen.Units -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Unit Converter Screen Active", color = Color(0xFF94A3B8))
                        }
                    }
                    is Screen.Currency -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Currency Converter Screen Active", color = Color(0xFF94A3B8))
                        }
                    }
                    is Screen.Constants -> ConstantsScreen(isDarkMode = isDarkMode)
                    is Screen.Estimator -> EstimatorScreen(isDarkMode = isDarkMode)
                }
            }
        }

        // Floating AI Assistant Button (FAB)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 96.dp)
        ) {
            AnimatedExecutiveFAB {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                showAiSheet = true
            }
        }

        // Fluid Glassmorphic Bottom Navigation Bar
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color(0xFF0F172A).copy(alpha = 0.85f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(80.dp)
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(Color(0xFF38BDF8).copy(alpha = 0.3f), Color(0xFF6366F1).copy(alpha = 0.1f))
                    ),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                screens.forEach { screen ->
                    val selected = currentScreen == screen
                    
                    val scaleAnim by animateFloatAsState(
                        targetValue = if (selected) 1.12f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "tab_scale"
                    )

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
                            .padding(vertical = 8.dp, horizontal = 10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (selected) 36.dp else 28.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selected) Color(0xFF38BDF8).copy(alpha = 0.15f)
                                    else Color.Transparent
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                tint = if (selected) Color(0xFF38BDF8) else Color(0xFF64748B),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = screen.title,
                            color = if (selected) Color(0xFF38BDF8) else Color(0xFF64748B),
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // AI Assistant Sheet Dialog Overlay
        if (showAiSheet) {
            AIAssistantSheet(
                onDismiss = { showAiSheet = false }
            )
        }
    }
}
