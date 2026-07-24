package com.example.luminarcalculator.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedThemeToggle(
    isDarkMode: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val infiniteTransition = rememberInfiniteTransition(label = "ThemeIconAnimation")

    if (isDarkMode) {
        // --- MOON ANIMATION: Gentle Breathing Aura & Soft Scale Pulse ---
        val moonPulseScale by infiniteTransition.animateFloat(
            initialValue = 0.92f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(2200, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "MoonPulse"
        )
        val moonAlpha by infiniteTransition.animateFloat(
            initialValue = 0.7f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(2200, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "MoonGlow"
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(40.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onToggle()
                    }
                )
        ) {
            Icon(
                imageVector = Icons.Default.NightsStay,
                contentDescription = "Switch to Light Mode",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
                    .scale(moonPulseScale)
                    .alpha(moonAlpha)
            )
        }
    } else {
        // --- SUN ANIMATION: Smooth Continuous Solar Ray Rotation ---
        val sunRotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(14000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "SunRotation"
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(40.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onToggle()
                    }
                )
        ) {
            Icon(
                imageVector = Icons.Default.LightMode,
                contentDescription = "Switch to Dark Mode",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(sunRotation)
            )
        }
    }
}
