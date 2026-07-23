package com.example.luminarcalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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

enum class LuminarButtonType {
    NUMBER,      // Dark Glass with Subtle Border
    OPERATOR,    // Glowing Cyan Gradient
    ACTION,      // Neon Green Text / Dark Background
    EQUALS       // Glowing Neon Emerald Action
}

@Composable
fun LuminarButton(
    symbol: String,
    type: LuminarButtonType,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // Cyber-Lux Color Palette
    val bgGradient = when (type) {
        LuminarButtonType.NUMBER -> listOf(Color(0xFF161C26), Color(0xFF0F141C))
        LuminarButtonType.OPERATOR -> listOf(Color(0xFF00E5FF), Color(0xFF0088FF))
        LuminarButtonType.ACTION -> listOf(Color(0xFF2A3447), Color(0xFF1E2636))
        LuminarButtonType.EQUALS -> listOf(Color(0xFF00FF9D), Color(0xFF00B36B))
    }

    val textColor = when (type) {
        LuminarButtonType.NUMBER -> Color.White
        LuminarButtonType.OPERATOR -> Color.Black
        LuminarButtonType.ACTION -> Color(0xFF00FF9D)
        LuminarButtonType.EQUALS -> Color.Black
    }

    val borderColor = when (type) {
        LuminarButtonType.NUMBER -> Color(0x33FFFFFF)
        LuminarButtonType.OPERATOR -> Color(0x8000E5FF)
        LuminarButtonType.ACTION -> Color(0x4000FF9D)
        LuminarButtonType.EQUALS -> Color(0x8000FF9D)
    }

    val glowColor = when (type) {
        LuminarButtonType.OPERATOR -> Color(0x4000E5FF)
        LuminarButtonType.EQUALS -> Color(0x6600FF9D)
        else -> Color.Transparent
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(5.dp)
            .shadow(
                elevation = if (type == LuminarButtonType.OPERATOR || type == LuminarButtonType.EQUALS) 10.dp else 2.dp,
                shape = CircleShape,
                spotColor = glowColor,
                ambientColor = glowColor
            )
            .clip(CircleShape)
            .background(brush = Brush.verticalGradient(bgGradient))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(listOf(borderColor, Color.Transparent)),
                shape = CircleShape
            )
            .clickable { onClick() }
    ) {
        Text(
            text = symbol,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
