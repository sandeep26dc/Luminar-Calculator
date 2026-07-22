package com.example.luminarcalculator.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.ui.theme.*

enum class ButtonType { STANDARD, OPERATOR, ACCENT, FUNCTION, SCIENTIFIC }

@Composable
fun CalculatorButton(
    text: String,
    type: ButtonType = ButtonType.STANDARD,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val view = LocalView.current

    val backgroundColor = when (type) {
        ButtonType.ACCENT -> ElectricBlue
        ButtonType.OPERATOR -> if (isDark) Color(0xFF162032) else Color(0xFFDCE5F0)
        ButtonType.FUNCTION -> if (isDark) Color(0xFF1A2230) else Color(0xFFE0E7F1)
        ButtonType.SCIENTIFIC -> if (isDark) Color(0xFF111927) else Color(0xFFD3DFEE)
        ButtonType.STANDARD -> if (isDark) DarkButtonSurface else LightButtonSurface
    }

    val textColor = when (type) {
        ButtonType.ACCENT -> AccentText
        ButtonType.OPERATOR -> ElectricBlue
        ButtonType.FUNCTION -> if (isDark) DarkSecondaryText else LightSecondaryText
        ButtonType.SCIENTIFIC -> if (isDark) ElectricBlue else Color(0xFF0056B3)
        ButtonType.STANDARD -> if (isDark) DarkButtonText else LightButtonText
    }

    Box(
        modifier = modifier
            .padding(4.dp)
            .shadow(
                elevation = if (type == ButtonType.ACCENT) 6.dp else 2.dp,
                shape = RoundedCornerShape(18.dp),
                clip = false
            )
            .clip(RoundedCornerShape(18.dp))
            .background(backgroundColor)
            .clickable {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = if (type == ButtonType.SCIENTIFIC) 15.sp else if (type == ButtonType.FUNCTION) 17.sp else 22.sp,
            fontWeight = if (type == ButtonType.ACCENT || type == ButtonType.OPERATOR) FontWeight.Bold else FontWeight.Medium
        )
    }
}
