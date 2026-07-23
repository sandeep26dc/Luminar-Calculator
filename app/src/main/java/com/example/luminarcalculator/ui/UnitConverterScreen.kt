package com.example.luminarcalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.ui.theme.*

@Composable
fun UnitConverterScreen(isDarkMode: Boolean) {
    var inputText by remember { mutableStateOf("1") }
    var selectedCategory by remember { mutableIntStateOf(0) }

    val categories = listOf("Length", "Weight", "Temp")
    val inputVal = inputText.toDoubleOrNull() ?: 0.0

    val textColor = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val subTextColor = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            categories.forEachIndexed { idx, cat ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (selectedCategory == idx) Color(0xFF00E5FF)
                            else (if (isDarkMode) Color(0xFF14181F) else Color(0xFFD6E3F2))
                        )
                        .clickable { selectedCategory = idx }
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = cat,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedCategory == idx) Color.Black else textColor
                    )
                }
            }
        }

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter Value", color = subTextColor) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00E5FF),
                unfocusedBorderColor = subTextColor,
                focusedLabelColor = Color(0xFF00E5FF),
                cursorColor = Color(0xFF00E5FF)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (selectedCategory) {
            0 -> {
                ConversionCard("Meters (m)", "$inputVal", isDarkMode)
                ConversionCard("Feet (ft)", String.format("%.3f", inputVal * 3.28084), isDarkMode)
                ConversionCard("Kilometers (km)", String.format("%.4f", inputVal / 1000.0), isDarkMode)
                ConversionCard("Miles (mi)", String.format("%.4f", inputVal / 1609.34), isDarkMode)
            }
            1 -> {
                ConversionCard("Kilograms (kg)", "$inputVal", isDarkMode)
                ConversionCard("Pounds (lbs)", String.format("%.3f", inputVal * 2.20462), isDarkMode)
                ConversionCard("Grams (g)", String.format("%.2f", inputVal * 1000.0), isDarkMode)
            }
            2 -> {
                ConversionCard("Celsius (°C)", "$inputVal", isDarkMode)
                ConversionCard("Fahrenheit (°F)", String.format("%.2f", (inputVal * 9/5) + 32), isDarkMode)
                ConversionCard("Kelvin (K)", String.format("%.2f", inputVal + 273.15), isDarkMode)
            }
        }
    }
}

@Composable
private fun ConversionCard(title: String, valStr: String, isDarkMode: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isDarkMode) Color(0xFF14181F) else Color(0xFFD6E3F2))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 16.sp, color = if (isDarkMode) DarkTextSecondary else LightTextSecondary)
        Text(text = valStr, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
    }
}
