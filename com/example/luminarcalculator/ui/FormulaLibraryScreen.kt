package com.example.luminarcalculator.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.FormulaLibraryEngine
import com.example.luminarcalculator.data.FormulaItem

@Composable
fun FormulaLibraryScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val items = remember(searchQuery) { FormulaLibraryEngine.search(searchQuery) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F172A), Color(0xFF090D16))
                )
            )
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Formula Knowledge Base",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Engineering & Scientific Reference Directory",
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp
                    )
                }
                TextButton(onClick = onBack) {
                    Text(text = "Back", color = Color(0xFF38BDF8))
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search equations (e.g. Bernoulli, Pipe)...", color = Color(0xFF475569)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF38BDF8),
                    unfocusedBorderColor = Color(0xFF334155),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            // Results List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(items) { formula ->
                    FormulaCard(formula)
                }
            }
        }
    }
}

@Composable
fun FormulaCard(item: FormulaItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.8f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF334155))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.category.uppercase(),
                    color = Color(0xFF38BDF8),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Equation Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F172A), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = item.formula,
                    color = Color(0xFF38BDF8),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = item.description,
                color = Color(0xFF94A3B8),
                fontSize = 13.sp
            )

            HorizontalDivider(color = Color(0xFF334155), thickness = 0.5.dp)

            // Variables mapping
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                item.variables.forEach { (k, v) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = k, color = Color(0xFF38BDF8), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = v, color = Color(0xFF64748B), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
