package com.example.luminarcalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.ui.theme.*

enum class ButtonRole { NUMBER, TOP_ROW, OPERATOR, ACCENT_EQUALS }

@Composable
fun NeumorphicButton(
    symbol: String,
    role: ButtonRole,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cornerShape = RoundedCornerShape(20.dp)

    // Dynamic color assignment based on Theme & Role
    val (bgColor, textColor, glowShadow) = when (role) {
        ButtonRole.NUMBER -> if (isDarkMode) {
            Triple(DarkKeyNumber, DarkTextPrimary, Color(0x40000000))
        } else {
            Triple(LightKeyNumber, LightTextPrimary, Color(0x2094A8C0))
        }

        ButtonRole.TOP_ROW -> if (isDarkMode) {
            Triple(DarkKeyTopRow, DarkTextSecondary, Color(0x30000000))
        } else {
            Triple(LightKeyTopRow, LightTextSecondary, Color(0x1594A8C0))
        }

        ButtonRole.OPERATOR -> if (isDarkMode) {
            Triple(DarkKeyOperator, DarkTextOperator, Color(0x40000000))
        } else {
            Triple(LightKeyOperator, LightTextOperator, Color(0x2094A8C0))
        }

        ButtonRole.ACCENT_EQUALS -> {
            Triple(DarkKeyAccent, Color.White, Color(0x660084FF))
        }
    }

    // Top border bevel simulating overhead light
    val borderBrush = if (role == ButtonRole.ACCENT_EQUALS) {
        Brush.verticalGradient(listOf(Color(0x80FFFFFF), Color.Transparent))
    } else if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0x22FFFFFF), Color(0x05FFFFFF)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0x10FFFFFF)))
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(5.dp)
            .shadow(
                elevation = if (role == ButtonRole.ACCENT_EQUALS) 8.dp else 4.dp,
                shape = cornerShape,
                ambientColor = glowShadow,
                spotColor = glowShadow
            )
            .clip(cornerShape)
            .background(bgColor)
            .border(width = 1.dp, brush = borderBrush, shape = cornerShape)
            .clickable { onClick() }
    ) {
        Text(
            text = symbol,
            fontSize = if (role == ButtonRole.TOP_ROW) 16.sp else 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
