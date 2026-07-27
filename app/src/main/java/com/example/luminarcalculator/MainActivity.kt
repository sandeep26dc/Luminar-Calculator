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
import com.example.luminarcalculator.ui.AnimatedExecutiveFAB
import com.example.luminarcalculator.ui.EngineeringScreen
import com.example.luminarcalculator.ui.theme.LuminarCalculatorTheme
import com.example.luminarcalculator.viewmodel.CalculatorViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LuminarCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("calculator") }
                    val calculations by viewModel.allCalculations.collectAsState(initial = emptyList())

                    Box(modifier = Modifier.fillMaxSize()) {
                        when (currentScreen) {
                            "calculator" -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Luminar Calculator",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Button(onClick = { currentScreen = "engineering" }) {
                                        Text("Open Engineering Modules")
                                    }
                                }
                            }
                            "engineering" -> {
                                EngineeringScreen(
                                    onBack = { currentScreen = "calculator" }
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(24.dp)
                        ) {
                            AnimatedExecutiveFAB(
                                onClick = {
                                    currentScreen = if (currentScreen == "calculator") "engineering" else "calculator"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
