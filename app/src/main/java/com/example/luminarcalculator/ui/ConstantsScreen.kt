package com.example.luminarcalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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

data class EngineeringConstant(
    val title: String,
    val category: String,
    val value: String,
    val description: String
)

@Composable
fun ConstantsScreen(isDarkMode: Boolean) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("All") }
    val haptic = LocalHapticFeedback.current

    val categories = listOf("All", "Materials", "Geotechnical", "Conversion", "Hydraulics")

    val constantsList = listOf(
        EngineeringConstant("Reinforced Concrete Density", "Materials", "24.0 - 25.0 kN/m³", "Standard unit weight for structural reinforced concrete design calculations."),
        EngineeringConstant("Plain Cement Concrete (PCC)", "Materials", "22.0 - 23.0 kN/m³", "Standard unit weight for unreinforced mass concrete beds."),
        EngineeringConstant("Structural Steel Density", "Materials", "7850 kg/m³", "Standard density value for structural steel sections and rebar estimation."),
        EngineeringConstant("Water Density", "Hydraulics", "1000 kg/m³ (1.0 g/cm³)", "Standard reference fluid density at 4°C for hydrostatic head calculations."),
        EngineeringConstant("Safe Bearing Capacity (Hard Rock)", "Geotechnical", " > 1000 kN/m²", "Presumptive safe bearing capacity for sound rock formations."),
        EngineeringConstant("Safe Bearing Capacity (Medium Soil)", "Geotechnical", "150 - 250 kN/m²", "Typical range for firm to stiff clay and compacted sandy soils."),
        EngineeringConstant("1 Barrel (oil) to Liters", "Conversion", "158.987 Liters", "Standard petroleum volume conversion factor."),
        EngineeringConstant("1 MPA to psi", "Conversion", "145.038 psi", "Pressure and stress unit conversion factor."),
        EngineeringConstant("Atmospheric Pressure", "Hydraulics", "101.325 kPa (1 atm)", "Standard sea-level atmospheric pressure reference."),
        EngineeringConstant("Modulus of Elasticity (Steel)", "Materials", "200 GPa", "Young's modulus for standard structural steel grades."),
        EngineeringConstant("Brick Masonry Unit Weight", "Materials", "19.0 - 20.0 kN/m³", "Standard weight for burnt clay brick masonry with mortar.")
    )

    val filteredList = constantsList.filter { item ->
        val matchesCategory = selectedCategory == "All" || item.category == selectedCategory
        val matchesSearch = item.title.contains(searchQuery, ignoreCase = true) ||
                item.value.contains(searchQuery, ignoreCase = true) ||
                item.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
    ) {
        // Search Filter Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF38BDF8)
                )
            },
            placeholder = {
                Text(
                    text = "Search handbook constants...",
                    color = Color(0xFF64748B),
                    fontSize = 14.sp
                )
            },
            textStyle = TextStyle(
                color = Color(0xFFF8FAFC),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF38BDF8),
                unfocusedBorderColor = Color(0xFF38BDF8).copy(alpha = 0.2f),
                focusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.3f),
                unfocusedContainerColor = Color(0xFF0F172A).copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category Filter Chips
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFF1E293B).copy(alpha = 0.5f),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                .padding(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            selectedCategory = cat
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFF38BDF8) else Color.Transparent,
                            contentColor = if (isSelected) Color(0xFF090D16) else Color(0xFF94A3B8)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                        elevation = if (isSelected) ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text(
                            text = cat,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Handbook Cards List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredList) { item ->
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFF1E293B).copy(alpha = 0.4f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(18.dp))
                        .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.15f), RoundedCornerShape(18.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF8FAFC),
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF38BDF8).copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = item.category.uppercase(),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF38BDF8)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = item.value,
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.description,
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }
    }
}
