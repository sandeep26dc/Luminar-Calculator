package com.example.luminarcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luminarcalculator.ui.CalculatorScreen
import com.example.luminarcalculator.ui.CalculatorViewModel
import com.example.luminarcalculator.ui.GraphScreen
import com.example.luminarcalculator.ui.UnitConverterScreen
import com.example.luminarcalculator.ui.components.AnimatedThemeToggle
import com.example.luminarcalculator.ui.components.ExecutiveInfoDialog
import com.example.luminarcalculator.ui.components.HistorySheet
import com.example.luminarcalculator.ui.theme.LuminarCalculatorTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkMode by rememberSaveable { mutableStateOf(true) }
            var showSplash by rememberSaveable { mutableStateOf(true) }

            // Short, premium startup sequence timer (approx 1.4 seconds)
            LaunchedEffect(key1 = true) {
                delay(1400L)
                showSplash = false
            }

            LuminarCalculatorTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showSplash) {
                        LuminarStartupAnimationScreen()
                    } else {
                        MainAppScreen(
                            isDarkMode = isDarkMode,
                            onToggleTheme = { isDarkMode = !isDarkMode }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LuminarStartupAnimationScreen() {
    val transitionState = remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        transitionState.value = true
    }

    val logoScale by animateFloatAsState(
        targetValue = if (transitionState.value) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "LogoScale"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (transitionState.value) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "LogoAlpha"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "MathSymbolsFloat")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FloatOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(32.dp)) {
            Text(
                text = "π",
                modifier = Modifier.align(Alignment.TopStart).offset(y = floatOffset.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                fontSize = 32.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "∑",
                modifier = Modifier.align(Alignment.TopEnd).offset(y = (-floatOffset).dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                fontSize = 32.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "√",
                modifier = Modifier.align(Alignment.BottomStart).offset(y = (-floatOffset).dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                fontSize = 32.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "∞",
                modifier = Modifier.align(Alignment.BottomEnd).offset(y = floatOffset.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                fontSize = 32.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = logoScale,
                    scaleY = logoScale,
                    alpha = logoAlpha
                )
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .size(80.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(28.dp)),
                shadowElevation = 12.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "L",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "L U M I N A R",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 8.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "PRECISION CALCULATOR",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 3.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

enum class ScreenTab { CALC, GRAPH, CONVERT }

@Composable
fun MainAppScreen(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    viewModel: CalculatorViewModel = viewModel()
) {
    var currentTab by rememberSaveable { mutableStateOf(ScreenTab.CALC) }
    var showInfoDialog by rememberSaveable { mutableStateOf(false) }
    var showHistorySheet by rememberSaveable { mutableStateOf(false) }
    
    val historyList by viewModel.allCalculations.collectAsState()
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
                        IconButton(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            showHistorySheet = true
                        }) {
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
                        ScreenTab.entries.forEach { tab ->
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
                        displayValue = viewModel.displayValue,
                        expressionValue = viewModel.expressionValue,
                        onButtonClick = { symbol ->
                            viewModel.onButtonClick(symbol)
                        }
                    )
                    ScreenTab.GRAPH -> GraphScreen(isDarkMode = isDarkMode)
                    ScreenTab.CONVERT -> UnitConverterScreen(isDarkMode = isDarkMode)
                }
            }
        }
    }

    // History Bottom Sheet Modal
    if (showHistorySheet) {
        HistorySheet(
            historyList = historyList,
            onClearHistory = { viewModel.clearHistory() },
            onDismiss = { showHistorySheet = false }
        )
    }

    // Executive Version History Modal Dialog
    if (showInfoDialog) {
        ExecutiveInfoDialog(onDismiss = { showInfoDialog = false })
    }
}
