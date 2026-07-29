package com.example.luminarcalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EstimatorScreen(isDarkMode: Boolean) {
    var selectedTab by rememberSaveable { mutableStateOf(0) } // 0: Concrete, 1: Rebar, 2: Bricks
    val haptic = LocalHapticFeedback.current

    val tabs = listOf("Concrete Volume", "Rebar Weight", "Masonry Blocks")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
    ) {
        // Sub-Navigation Tabs for Estimator Types
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E293B).copy(alpha = 0.5f),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                .padding(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            selectedTab = index
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFF38BDF8) else Color.Transparent,
                            contentColor = if (isSelected) Color(0xFF090D16) else Color(0xFF94A3B8)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 10.dp),
                        elevation = if (isSelected) ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text(
                            text = title,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Dynamic Sub-Screen based on selected tab
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                when (selectedTab) {
                    0 -> ConcreteEstimatorCard()
                    1 -> RebarEstimatorCard()
                    2 -> BrickEstimatorCard()
                }
            }
        }
    }
}

@Composable
fun ConcreteEstimatorCard() {
    var length by rememberSaveable { mutableStateOf("5.0") }
    var width by rememberSaveable { mutableStateOf("4.0") }
    var depth by rememberSaveable { mutableStateOf("0.15") }
    var wastagePercent by rememberSaveable { mutableStateOf("5.0") }

    val l = length.toDoubleOrNull() ?: 0.0
    val w = width.toDoubleOrNull() ?: 0.0
    val d = depth.toDoubleOrNull() ?: 0.0
    val waste = wastagePercent.toDoubleOrNull() ?: 0.0

    val netVolume = l * w * d
    val totalVolume = netVolume * (1.0 + (waste / 100.0))
    // Standard thumb rules: Cement ~ 7 bags per m3 of M20/M25 concrete, Sand ~ 0.45 m3, Aggregate ~ 0.90 m3
    val cementBags = totalVolume * 7.0
    val sandM3 = totalVolume * 0.45
    val aggM3 = totalVolume * 0.90

    EstimatorWrapper(title = "Concrete & Screed Calculator", subtitle = "Calculate wet volume and material component breakdown.") {
        EstimatorTextField(label = "Length (m)", value = length, onValueChange = { length = it })
        EstimatorTextField(label = "Width / Span (m)", value = width, onValueChange = { width = it })
        EstimatorTextField(label = "Thickness / Depth (m)", value = depth, onValueChange = { depth = it })
        EstimatorTextField(label = "Wastage Allowance (%)", value = wastagePercent, onValueChange = { wastagePercent = it })

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFF334155))
        Spacer(modifier = Modifier.height(8.dp))

        ResultRow("Net Volume:", String.format("%.3f m³", netVolume))
        ResultRow("Total Volume (incl. waste):", String.format("%.3f m³", totalVolume))
        Spacer(modifier = Modifier.height(4.dp))
        ResultRow("Est. Cement Bags (50kg):", String.format("%.1f bags", cementBags))
        ResultRow("Est. Sand Quantity:", String.format("%.2f m³", sandM3))
        ResultRow("Est. Aggregate Quantity:", String.format("%.2f m³", aggM3))
    }
}

@Composable
fun RebarEstimatorCard() {
    var barDiameter by rememberSaveable { mutableStateOf("16") } // mm
    var totalLengthMeters by rememberSaveable { mutableStateOf("100") }

    val dia = barDiameter.toDoubleOrNull() ?: 0.0
    val length = totalLengthMeters.toDoubleOrNull() ?: 0.0
    // Standard rebar unit weight formula: Weight (kg) = (D^2 / 162.2) * Length (m)
    val unitWeightPerMeter = if (dia > 0) (dia * dia) / 162.2 else 0.0
    val totalWeightKg = unitWeightPerMeter * length

    EstimatorWrapper(title = "Steel Rebar Weight Estimator", subtitle = "Standard theoretical mass calculation based on bar diameter.") {
        EstimatorTextField(label = "Bar Diameter (mm) e.g., 10, 12, 16, 20", value = barDiameter, onValueChange = { barDiameter = it })
        EstimatorTextField(label = "Total Length (meters)", value = totalLengthMeters, onValueChange = { totalLengthMeters = it })

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFF334155))
        Spacer(modifier = Modifier.height(8.dp))

        ResultRow("Unit Weight:", String.format("%.3f kg/m", unitWeightPerMeter))
        ResultRow("Total Estimated Weight:", String.format("%.2f kg (%.3f tonnes)", totalWeightKg, totalWeightKg / 1000.0))
    }
}

@Composable
fun BrickEstimatorCard() {
    var wallLength by rememberSaveable { mutableStateOf("10.0") }
    var wallHeight by rememberSaveable { mutableStateOf("3.0") }
    var brickThickness by rememberSaveable { mutableStateOf("0.2") } // 200mm wall

    val l = wallLength.toDoubleOrNull() ?: 0.0
    val h = wallHeight.toDoubleOrNull() ?: 0.0
    val t = brickThickness.toDoubleOrNull() ?: 0.2

    val wallVolume = l * h * t
    // Standard standard brick size with mortar (~500 bricks per m3 of brickwork)
    val totalBricks = wallVolume * 500.0
    val mortarM3 = wallVolume * 0.30 // ~30% mortar volume

    EstimatorWrapper(title = "Masonry Block & Brick Estimator", subtitle = "Calculate total block counts and mortar volume for walls.") {
        EstimatorTextField(label = "Wall Length (m)", value = wallLength, onValueChange = { wallLength = it })
        EstimatorTextField(label = "Wall Height (m)", value = wallHeight, onValueChange = { wallHeight = it })
        EstimatorTextField(label = "Wall Thickness (m)", value = brickThickness, onValueChange = { brickThickness = it })

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFF334155))
        Spacer(modifier = Modifier.height(8.dp))

        ResultRow("Total Wall Volume:", String.format("%.2f m³", wallVolume))
        ResultRow("Estimated Block Count:", String.format("%.0f blocks", totalBricks))
        ResultRow("Estimated Wet Mortar:", String.format("%.2f m³", mortarM3))
    }
}

@Composable
fun EstimatorWrapper(
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF1E293B).copy(alpha = 0.4f),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.15f), RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF8FAFC)
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF94A3B8)
            )
            Spacer(modifier = Modifier.height(4.dp))
            content()
        }
    }
}

@Composable
fun EstimatorTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = Color(0xFF94A3B8), fontSize = 12.sp) },
        textStyle = TextStyle(
            color = Color(0xFFF8FAFC),
            fontSize = 15.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF38BDF8),
            unfocusedBorderColor = Color(0xFF38BDF8).copy(alpha = 0.2f),
            focusedContainerColor = Color(0xFF0F172A).copy(alpha = 0.4f),
            unfocusedContainerColor = Color(0xFF0F172A).copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFF94A3B8),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 15.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF38BDF8),
            fontWeight = FontWeight.Bold
        )
    }
}
