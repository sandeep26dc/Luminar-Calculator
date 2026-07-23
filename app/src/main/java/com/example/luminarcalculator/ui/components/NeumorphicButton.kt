package com.example.luminarcalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.ui.theme.*

enum class ButtonRole { NUMBER, OPERATOR, TOP_ROW, ACCENT_EQUALS }

@Composable
fun NeumorphicButton(
    text: String,
    role: ButtonRole,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Tactile haptic triggering on click
    val handleTouch = {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        onClick()
    }

    val bgColor = when (role) {
        ButtonRole.NUMBER -> if (isDarkMode) DarkKeyNum else LightKeyNum
        ButtonRole.OPERATOR -> if (isDarkMode) DarkKeyOp else LightKeyOp
        ButtonRole.TOP_ROW -> if (isDarkMode) DarkKeyTop else LightKeyTop
        ButtonRole.ACCENT_EQUALS -> if (isDarkMode) DarkKeyAccent else LightKeyAccent
    }

    val textColor = when (role) {
        ButtonRole.ACCENT_EQUALS -> Color.White
        ButtonRole.OPERATOR -> Color(0xFF00E5FF)
        else -> if (isDarkMode) DarkTextPrimary else LightTextPrimary
    }

    val elevation = if (isPressed) 1.dp else 4.dp

    Box(
        modifier = modifier
            .padding(4.dp)
            .height(60.dp)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(18.dp),
                clip = false
            )
            .clip(RoundedCornerShape(18.dp))
            .background(bgColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = handleTouch
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
