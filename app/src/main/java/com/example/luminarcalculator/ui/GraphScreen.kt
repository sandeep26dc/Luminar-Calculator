package com.example.luminarcalculator.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.CalculatorEngine
import com.example.luminarcalculator.ui.theme.*

@Composable
fun GraphScreen(isDarkMode: Boolean) {
    var functionInput by rememberSaveable { mutableStateOf("sin(x)") }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var scale by remember { mutableFloatStateOf(50f) }

    val bgColor = if (isDarkMode) DarkBackground else LightBackground
    val gridColor = if (isDarkMode) Color(0x22FFFFFF) else Color(0x22000000)
    val axisColor = if (isDarkMode) Color(0x80FFFFFF) else Color(0x80000000)
    val lineCurveColor = Color(0xFF00E5FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = functionInput,
            onValueChange = { functionInput = it },
            label = { Text("Plot f(x)", color = if (isDarkMode) DarkTextSecondary else LightTextSecondary) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = lineCurveColor,
                unfocusedBorderColor = gridColor,
                focusedTextColor = if (isDarkMode) DarkTextPrimary else LightTextPrimary,
                unfocusedTextColor = if (isDarkMode) DarkTextPrimary else LightTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .background(if (isDarkMode) Color(0xFF0B0E14) else Color(0xFFF0F5FA))
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(10f, 300f)
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val centerX = width / 2f + offsetX
                val centerY = height / 2f + offsetY

                // Grid Lines
                var x = centerX % scale
                while (x < width) {
                    drawLine(gridColor, Offset(x, 0f), Offset(x, height), strokeWidth = 1f)
                    x += scale
                }
                var y = centerY % scale
                while (y < height) {
                    drawLine(gridColor, Offset(0f, y), Offset(width, y), strokeWidth = 1f)
                    y += scale
                }

                // Coordinate Axes
                drawLine(axisColor, Offset(0f, centerY), Offset(width, centerY), strokeWidth = 2f)
                drawLine(axisColor, Offset(centerX, 0f), Offset(centerX, height), strokeWidth = 2f)

                // Render Math Path
                val path = Path()
                var isFirstPoint = true

                for (pixelX in 0..width.toInt() step 2) {
                    val mathX = (pixelX - centerX) / scale
                    val mathY = CalculatorEngine.evaluateForGraph(functionInput, mathX.toDouble())

                    if (mathY != null && !mathY.isNaN()) {
                        val pixelY = centerY - (mathY.toFloat() * scale)
                        if (pixelY in -500f..(height + 500f)) {
                            if (isFirstPoint) {
                                path.moveTo(pixelX.toFloat(), pixelY)
                                isFirstPoint = false
                            } else {
                                path.lineTo(pixelX.toFloat(), pixelY)
                            }
                        } else {
                            isFirstPoint = true
                        }
                    } else {
                        isFirstPoint = true
                    }
                }

                drawPath(
                    path = path,
                    color = lineCurveColor,
                    style = Stroke(width = 4f)
                )
            }

            Text(
                text = "Pinch to Zoom • Drag to Move",
                fontSize = 11.sp,
                color = if (isDarkMode) Color(0x66FFFFFF) else Color(0x66000000),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
    }
}
