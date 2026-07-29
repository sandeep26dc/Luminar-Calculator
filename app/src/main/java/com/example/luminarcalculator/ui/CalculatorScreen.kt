package com.example.luminarcalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.ui.components.NeumorphicButton

@Composable
fun CalculatorScreen(
    displayValue: String,
    expressionValue: String,
    onButtonClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF090D16),
                        Color(0xFF0F172A),
                        Color(0xFF090D16)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Glassmorphic Blended Display Area (No heavy boxed look)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1E293B).copy(alpha = 0.35f),
                                Color(0xFF0F172A).copy(alpha = 0.15f)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF38BDF8).copy(alpha = 0.25f),
                                Color(0xFF1E293B).copy(alpha = 0.05f)
                            )
                        ),
                        RoundedCornerShape(28.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Expression / History Trace
                    Text(
                        text = expressionValue,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF94A3B8).copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Primary Calculation Result Output
                    Text(
                        text = displayValue,
                        fontSize = 48.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF8FAFC),
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Executive 3D Projected Keypad Matrix
            val buttons = listOf(
                listOf("C", "()", "%", "÷"),
                listOf("7", "8", "9", "×"),
                listOf("4", "5", "6", "−"),
                listOf("1", "2", "3", "+"),
                listOf("+/−", "0", ".", "=")
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                buttons.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        row.forEach { symbol ->
                            val isOperator = symbol in listOf("÷", "×", "−", "+", "=")
                            val isAction = symbol in listOf("C", "()", "%")

                            NeumorphicButton(
                                text = symbol,
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1.1f),
                                isPrimary = isOperator,
                                textColor = if (isAction) Color(0xFF38BDF8) else null,
                                onClick = { onButtonClick(symbol) }
                            )
                        }
                    }
                }
            }
        }
    }
}
