package com.example.luminarcalculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ExecCyanAccentDark,
    background = ExecDarkBackground,
    surface = ExecDarkSurface,
    surfaceVariant = ExecDarkBorder,
    onBackground = ExecDarkOnBackground,
    onSurface = ExecDarkOnBackground,
    onSurfaceVariant = ExecDarkOnSurfaceSubtle
)

private val LightColorScheme = lightColorScheme(
    primary = ExecCyanAccentLight,
    background = ExecLightBackground,
    surface = ExecLightSurface,
    surfaceVariant = ExecLightBorder,
    onBackground = ExecLightOnBackground,
    onSurface = ExecLightOnBackground,
    onSurfaceVariant = ExecLightOnSurfaceSubtle
)

@Composable
fun LuminarCalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
