package com.example.luminarcalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.ConverterAndHistory
import com.example.luminarcalculator.data.ConversionResult

@Composable
fun UnitConverterScreen(isDarkMode: Boolean) {
    var category by rememberSaveable { mutableStateOf("Length") }
    var inputValue by rememberSaveable { mutableStateOf("1000") }
    val haptic = LocalHapticFeedback.current

    val categories = listOf("Length", "Weight", "Temp")
    val conversions: List<ConversionResult> = ConverterAndHistory.convert(category, inputValue.toDoubleOrNull() ?: 0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
    ) {
        // Executive Segmented Tab Selector
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
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = category == cat
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            category = cat
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFF38BDF8) else Color.Transparent,
                            contentColor = if (isSelected) Color(0xFF090D16) else Color(0xFF94A3B8)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = if (isSelected) ButtonDefaults.buttonElevation(defaultElevation = 4.dp) else ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text(
                            text = cat,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Executive Glowing High-Contrast Input Field
        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { 
                Text(
                    text = "ENTER VALUE", 
                    letterSpacing = 2.sp,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8)
                ) 
            },
            textStyle = TextStyle(
                color = Color(0xFFF8FAFC),
                fontSize = 26.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF38BDF8),
                unfocusedBorderColor = Color(0xFF38BDF8).copy(alpha = 0.2f),
                focusedTextColor = Color(0xFFF8FAFC),
                unfocusedTextColor = Color(0xFFF8FAFC),
                focusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.3f),
                unfocusedContainerColor = Color(0xFF0F172A).copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(18.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Executive Glass Card Results
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(conversions) { item ->
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFF1E293B).copy(alpha = 0.4f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(18.dp))
                        .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.15f), RoundedCornerShape(18.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.unitName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = String.format("%.4f", item.value),
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )
                    }
                }
            }
        }
    }
}
