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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.UnitRateComparison
import com.example.luminarcalculator.ui.components.UnitRateCard

@Composable
fun UnitRateScreen(isDarkMode: Boolean) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedFilter by rememberSaveable { mutableStateOf("All") }

    // Sample engineering BOQ / unit rate items (easily linkable to your actual project data files)
    val sampleUnitRates = remember {
        listOf(
            UnitRateComparison("CON-001", "Ready Mix Concrete (M35 Grade)", "m³", 350.0, 365.0, 120.0),
            UnitRateComparison("STE-002", "High Yield Steel Reinforcement Bars", "MT", 2400.0, 2350.0, 45.0),
            UnitRateComparison("PIP-003", "Carbon Steel Piping 4\" Schedule 40", "Rm", 85.0, 92.5, 850.0),
            UnitRateComparison("ELE-004", "XLPE Armoured Copper Cable 4Cx95mm²", "m", 120.0, 115.0, 400.0),
            UnitRateComparison("EAR-005", "Excavation in Hard Rock & Disposal", "m³", 45.0, 52.0, 650.0),
            UnitRateComparison("FOR-006", "System Formwork for Columns/Walls", "m²", 55.0, 55.0, 300.0)
        )
    }

    val filters = listOf("All", "Over Budget", "Savings", "Aligned")

    val filteredList = sampleUnitRates.filter { item ->
        val matchesSearch = item.itemCode.contains(searchQuery, ignoreCase = true) ||
                item.itemName.contains(searchQuery, ignoreCase = true)
        
        val matchesFilter = when (selectedFilter) {
            "Over Budget" -> item.variancePercentage > 1.0
            "Savings" -> item.variancePercentage < -1.0
            "Aligned" -> item.variancePercentage in -1.0..1.0
            else -> true
        }

        matchesSearch && matchesFilter
    }

    // Summary calculations
    val totalBaselineBudget = sampleUnitRates.sumOf { it.totalBaselineCost }
    val totalActualCost = sampleUnitRates.sumOf { it.totalActualCost }
    val netVariance = totalActualCost - totalBaselineBudget

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
    ) {
        // Header Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "UNIT RATE VARIANCE",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Project Cost Tracking",
                    color = Color(0xFFF8FAFC),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Executive Financial Summary Banner Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E293B).copy(alpha = 0.5f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Total Baseline", fontSize = 11.sp, color = Color(0xFF94A3B8))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = String.format("%.2f", totalBaselineBudget),
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF8FAFC)
                    )
                }

                Column {
                    Text(text = "Total Actual", fontSize = 11.sp, color = Color(0xFF94A3B8))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = String.format("%.2f", totalActualCost),
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Net Variance", fontSize = 11.sp, color = Color(0xFF94A3B8))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = (if (netVariance >= 0) "+" else "") + String.format("%.2f", netVariance),
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (netVariance > 0) Color(0xFFEF4444) else Color(0xFF10B981)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { 
                Text(
                    text = "Search item code or description...",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8)
                ) 
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF38BDF8)
                )
            },
            textStyle = TextStyle(
                color = Color(0xFFF8FAFC),
                fontSize = 14.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF38BDF8),
                unfocusedBorderColor = Color(0xFF38BDF8).copy(alpha = 0.2f),
                focusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.3f),
                unfocusedContainerColor = Color(0xFF0F172A).copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Filter Chips Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                val isSelected = selectedFilter == filter
                Surface(
                    onClick = { selectedFilter = filter },
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) Color(0xFF38BDF8) else Color(0xFF1E293B).copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, if (isSelected) Color(0xFF38BDF8) else Color(0xFF334155)),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filter,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF090D16) else Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Item Cards List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredList) { comparison ->
                UnitRateCard(comparison = comparison)
            }
        }
    }
}
