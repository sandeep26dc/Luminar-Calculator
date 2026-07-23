package com.example.luminarcalculator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = DarkBackground,
    surface = DarkKeyNum,
    primary = DarkKeyAccent,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextSecondary
)

private val LightColorScheme = lightColorScheme(
    background = LightBackground,
    surface = LightKeyNum,
    primary = LightKeyAccent,
    onBackground = LightTextPrimary,
    onSurface = LightTextSecondary
)

@Composable
fun LuminarTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
