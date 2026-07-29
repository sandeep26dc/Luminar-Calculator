package com.example.luminarcalculator.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EngineeringScreen(
    onBack: () -> Unit,
    onNavigateToFormulas: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Civil") }
    var param1 by remember { mutableStateOf("5.0") }
    var param2 by remember { mutableStateOf("4.0") }

    val primaryMetric: String
    val secondaryMetrics: Map<String, String>

    when (selectedTab) {
        "Civil" -> {
            primaryMetric = "20.0 m²"
            secondaryMetrics = mapOf("Perimeter" to "18.0 m", "Volume (h=0.15m)" to "3.0 m³")
        }
        "Mechanical" -> {
            primaryMetric = "74.83 kg"
            secondaryMetrics = mapOf("Outer Diameter" to "${param1}mm", "Length" to "${param2}m", "Material" to "Carbon Steel")
        }
        else -> {
            primaryMetric = "12.0 Amps"
            secondaryMetrics = mapOf("Voltage" to "${param1}V", "Resistance" to "${param2}Ω", "Power" to "1440 W")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFFF8FAFC))
                }
                Text(
                    text = "ENGINEERING MODULES",
                    color = Color(0xFFF8FAFC),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }
            IconButton(onClick = onNavigateToFormulas) {
                Icon(imageVector = Icons.Default.Book, contentDescription = "Formula Library", tint = Color(0xFF38BDF8))
            }
        }

        // Tab Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Civil", "Mechanical", "Electrical").forEach { tab ->
                val isSelected = selectedTab == tab
                Button(
                    onClick = { selectedTab = tab },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFF38BDF8) else Color(0xFF1E293B).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = tab,
                        color = if (isSelected) Color(0xFF090D16) else Color(0xFF94A3B8),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Input Parameters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = param1,
                onValueChange = { param1 = it },
                label = { Text("Parameter 1") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF38BDF8),
                    unfocusedBorderColor = Color(0xFF38BDF8).copy(alpha = 0.2f),
                    focusedTextColor = Color(0xFFF8FAFC),
                    unfocusedTextColor = Color(0xFFF8FAFC)
                ),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = param2,
                onValueChange = { param2 = it },
                label = { Text("Parameter 2") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF38BDF8),
                    unfocusedBorderColor = Color(0xFF38BDF8).copy(alpha = 0.2f),
                    focusedTextColor = Color(0xFFF8FAFC),
                    unfocusedTextColor = Color(0xFFF8FAFC)
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Result Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.4f)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.15f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "PRIMARY RESULT", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = primaryMetric, color = Color(0xFFF8FAFC), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)

                HorizontalDivider(color = Color(0xFF38BDF8).copy(alpha = 0.15f))

                secondaryMetrics.forEach { entry ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = entry.key, color = Color(0xFF94A3B8), fontSize = 13.sp)
                        Text(text = entry.value, color = Color(0xFFF8FAFC), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
