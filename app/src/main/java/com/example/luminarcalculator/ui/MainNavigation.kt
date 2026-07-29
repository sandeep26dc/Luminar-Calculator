package com.example.luminarcalculator.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController(),
    isDarkMode: Boolean = false,
    onThemeToggle: () -> Unit = {}
) {
    val viewModel: CalculatorViewModel = viewModel()

    NavHost(navController = navController, startDestination = "calculator") {
        composable("calculator") {
            CalculatorScreen(
                viewModel = viewModel,
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle,
                onNavigateToEngineering = { navController.navigate("engineering") },
                onNavigateToConverter = { navController.navigate("converter") },
                onNavigateToHistory = { navController.navigate("history") }
            )
        }
        composable("engineering") {
            EngineeringScreen(
                isDarkMode = isDarkMode,
                onBack = { navController.popBackStack() }
            )
        }
        composable("converter") {
            ConverterScreen(
                isDarkMode = isDarkMode,
                onBack = { navController.popBackStack() }
            )
        }
        composable("history") {
            HistoryScreen(
                viewModel = viewModel,
                isDarkMode = isDarkMode,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
