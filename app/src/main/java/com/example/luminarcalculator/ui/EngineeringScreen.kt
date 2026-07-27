package com.example.luminarcalculator.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.EngineeringCategory
import com.example.luminarcalculator.data.EngineeringEngine
import com.example.luminarcalculator.data.EngineeringResult
import java.math.BigDecimal

@Composable
fun EngineeringScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Civil, 1: Mechanical, 2: Electrical

    // Input States - Civil
    var civilLength by remember { mutableStateOf("10.0") }
    var civilWidth by remember { mutableStateOf("5.0") }
    var civilDepth by remember { mutableStateOf("0.3") }

    // Input States - Mechanical Pipe
    var pipeOd by remember { mutableStateOf("219.1") }
    var pipeThick by remember { mutableStateOf("8.18") }
    var pipeLength by remember { mutableStateOf("12.0") }

    // Input States - Electrical Ohm's Law
    var volt by remember { mutableStateOf("415") }
    var current by remember { mutableStateOf("25") }
    var resistance by remember { mutableStateOf("") }

    // Compute active result
    val currentResult: EngineeringResult = remember(selectedTab, civilLength, civilWidth, civilDepth, pipeOd, pipeThick, pipeLength, volt, current, resistance) {
        try {
            when (selectedTab) {
                0 -> EngineeringEngine.calculate(
                    EngineeringCategory.Civil(
                        lengthMeters = civilLength.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                        widthMeters = civilWidth.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                        depthMeters = civilDepth.toBigDecimalOrNull() ?: BigDecimal.ZERO
                    )
                )
                1 -> EngineeringEngine.calculate(
                    EngineeringCategory.MechanicalPipe(
                        outerDiameterMm = pipeOd.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                        wallThicknessMm = pipeThick.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                        lengthMeters = pipeLength.toBigDecimalOrNull() ?: BigDecimal.ZERO
                    )
                )
                else -> EngineeringEngine.calculate(
                    EngineeringCategory.ElectricalOhm(
                        voltageV = volt.toBigDecimalOrNull(),
                        currentA = current.toBigDecimalOrNull(),
                        resistanceOhm = resistance.toBigDecimalOrNull()
                    )
                )
            }
        } catch (e: Exception) {
            EngineeringResult("Calculation Error", "Invalid Input", emptyMap())
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF090D16))
                )
            )
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Luminar Engineering",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onBack) {
                    Text(text = "Back to Calc", color = Color(0xFF38BDF8))
                }
            }

            // Category Segment Switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF1E293B))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TabButton(title = "Civil", isSelected = selectedTab == 0) { selectedTab = 0 }
                TabButton(title = "Mechanical", isSelected = selectedTab == 1) { selectedTab = 1 }
                TabButton(title = "Electrical", isSelected = selectedTab == 2) { selectedTab = 2 }
            }

            // Dynamic Inputs Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.7f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = when (selectedTab) {
                            0 -> "Concrete & Slab Parameters (m)"
                            1 -> "Pipe & Structural Specs (mm / m)"
                            else -> "Ohm's Law Matrix (Leave one blank to compute)"
                        },
                        color = Color(0xFF94A3B8),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    when (selectedTab) {
                        0 -> {
                            EngInputField(label = "Length (m)", value = civilLength) { civilLength = it }
                            EngInputField(label = "Width (m)", value = civilWidth) { civilWidth = it }
                            EngInputField(label = "Depth / Thickness (m)", value = civilDepth) { civilDepth = it }
                        }
                        1 -> {
                            EngInputField(label = "Outer Diameter (mm)", value = pipeOd) { pipeOd = it }
                            EngInputField(label = "Wall Thickness (mm)", value = pipeThick) { pipeThick = it }
                            EngInputField(label = "Length (m)", value = pipeLength) { pipeLength = it }
                        }
                        2 -> {
                            EngInputField(label = "Voltage (V)", value = volt) { volt = it }
                            EngInputField(label = "Current (A)", value = current) { current = it }
                            EngInputField(label = "Resistance (Ω)", value = resistance) { resistance = it }
                        }
                    }
                }
            }

            // Result Display Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = currentResult.title.uppercase(),
                        color = Color(0xFF38BDF8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    
                    Text(
                        text = currentResult.primaryMetric,
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    HorizontalDivider(color = Color(0xFF334155), thickness = 1.dp)

                    // Secondary Metrics Row/Grid
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        currentResult.secondaryMetrics.forEach { (key, value) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = key, color = Color(0xFF94A3B8), fontSize = 14.sp)
                                Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabButton(title: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) Color(0xFF38BDF8) else Color.Transparent
    val textColor = if (isSelected) Color(0xFF0F172A) else Color(0xFF94A3B8)
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun EngInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color(0xFF64748B)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF38BDF8),
            unfocusedBorderColor = Color(0xFF334155),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color(0xFF38BDF8)
        ),
        shape = RoundedCornerShape(12.dp)
    )
}
