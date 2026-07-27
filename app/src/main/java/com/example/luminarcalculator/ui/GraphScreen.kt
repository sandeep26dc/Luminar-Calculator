package com.example.luminarcalculator.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.objecthunter.exp4j.ExpressionBuilder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphScreen(isDarkMode: Boolean) {
    var functionInput by remember { mutableStateOf("sin(x)") }
    var evaluatedExpression by remember { mutableStateOf("sin(x)") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Panning and Zooming states
    var scale by remember { mutableStateOf(40f) } // Pixels per unit
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val errorColor = MaterialTheme.colorScheme.error
    val gridColor = surfaceVariantColor.copy(alpha = 0.5f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(20.dp)
    ) {
        // Executive Function Input Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, surfaceVariantColor, RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "f(x) =",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor,
                    fontSize = 16.sp
                )
                OutlinedTextField(
                    value = functionInput,
                    onValueChange = { functionInput = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 16.sp,
                        color = onSurfaceColor
                    )
                )
                Button(
                    onClick = {
                        try {
                            ExpressionBuilder(functionInput).variable("x").build()
                            evaluatedExpression = functionInput
                            errorMessage = null
                        } catch (e: Exception) {
                            errorMessage = "Invalid Expression"
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text("Plot", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Error Banner if invalid expression
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage!!,
                color = errorColor,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Executive Interactive Graph Canvas Box with Pan & Zoom
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .shadow(8.dp, RoundedCornerShape(24.dp))
                .border(1.dp, surfaceVariantColor, RoundedCornerShape(24.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(10f, 200f)
                            offsetX += pan.x
                            offsetY += pan.y
                        }
                    }
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    val centerX = width / 2f + offsetX
                    val centerY = height / 2f + offsetY

                    // Draw Grid Lines
                    val step = scale
                    if (step > 5f) {
                        var xCoord = centerX % step
                        while (xCoord < width) {
                            drawLine(
                                color = gridColor.copy(alpha = 0.2f),
                                start = Offset(xCoord, 0f),
                                end = Offset(xCoord, height),
                                strokeWidth = 1f
                            )
                            xCoord += step
                        }
                        var yCoord = centerY % step
                        while (yCoord < height) {
                            drawLine(
                                color = gridColor.copy(alpha = 0.2f),
                                start = Offset(0f, yCoord),
                                end = Offset(width, yCoord),
                                strokeWidth = 1f
                            )
                            yCoord += step
                        }
                    }

                    // Precision Axis Lines
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, centerY),
                        end = Offset(width, centerY),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = gridColor,
                        start = Offset(centerX, 0f),
                        end = Offset(centerX, height),
                        strokeWidth = 2f
                    )

                    // Render High-Precision Dynamic Function Curve using exp4j
                    try {
                        val expression = ExpressionBuilder(evaluatedExpression).variable("x").build()
                        val path = Path()
                        var first = true

                        val pixelStep = 2f
                        var px = 0f
                        while (px <= width) {
                            val xVal = (px - centerX) / scale
                            expression.setVariable("x", xVal.toDouble())
                            val yVal = expression.evaluate()

                            if (!yVal.isNaN() && !yVal.isInfinite()) {
                                val py = centerY - (yVal.toFloat() * scale)
                                if (first) {
                                    path.moveTo(px, py)
                                    first = false
                                } else {
                                    path.lineTo(px, py)
                                }
                            } else {
                                first = true
                            }
                            px += pixelStep
                        }

                        drawPath(
                            path = path,
                            color = primaryColor,
                            style = Stroke(width = 4f)
                        )
                    } catch (e: Exception) {
                        // Suppress interim bad equations during typing
                    }
                }

                // Instructions Overlay Hint
                Text(
                    text = "Drag to pan • Pinch to zoom",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(12.dp),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = onSurfaceColor.copy(alpha = 0.4f)
                )
            }
        }
    }
}
