package com.example.luminarcalculator.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.FormulaEntity

@Composable
fun FormulaLibraryScreen(
    viewModel: CalculatorViewModel,
    onBack: () -> Unit
) {
    val formulas by viewModel.allFormulas.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }

    // Input states for adding custom formula
    var titleInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("") }
    var formulaInput by remember { mutableStateOf("") }
    var variablesInput by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFFF8FAFC)
                    )
                }
                Text(
                    text = "FORMULA LIBRARY",
                    color = Color(0xFFF8FAFC),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }

            // Formula List connected to Room Database
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(formulas) { item ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(18.dp))
                            .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.15f), RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFF1E293B).copy(alpha = 0.4f)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.title,
                                    color = Color(0xFFF8FAFC),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF38BDF8).copy(alpha = 0.15f),
                                        modifier = Modifier.padding(end = 8.dp)
                                    ) {
                                        Text(
                                            text = item.category,
                                            color = Color(0xFF38BDF8),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.deleteFormula(item) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Formula",
                                            tint = Color(0xFFEF4444).copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = item.formula,
                                color = Color(0xFF38BDF8),
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )

                            HorizontalDivider(color = Color(0xFF38BDF8).copy(alpha = 0.15f))

                            Text(
                                text = "Variables: ${item.variablesString}",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Button to Add New Custom Formula
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF38BDF8),
            contentColor = Color(0xFF090D16),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Formula")
        }

        // Add Formula Dialog
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                containerColor = Color(0xFF0F172A),
                titleContentColor = Color(0xFFF8FAFC),
                textContentColor = Color(0xFF94A3B8),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.3f), RoundedCornerShape(24.dp)),
                title = { Text("Add Custom Formula", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = titleInput,
                            onValueChange = { titleInput = it },
                            label = { Text("Title (e.g., Concrete Volume)") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF38BDF8),
                                unfocusedBorderColor = Color(0xFF38BDF8).copy(alpha = 0.2f),
                                focusedTextColor = Color(0xFFF8FAFC),
                                unfocusedTextColor = Color(0xFFF8FAFC),
                                focusedLabelColor = Color(0xFF38BDF8)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = categoryInput,
                            onValueChange = { categoryInput = it },
                            label = { Text("Category (e.g., Civil)") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF38BDF8),
                                unfocusedBorderColor = Color(0xFF38BDF8).copy(alpha = 0.2f),
                                focusedTextColor = Color(0xFFF8FAFC),
                                unfocusedTextColor = Color(0xFFF8FAFC),
                                focusedLabelColor = Color(0xFF38BDF8)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = formulaInput,
                            onValueChange = { formulaInput = it },
                            label = { Text("Formula Expression (e.g., L × W × H)") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF38BDF8),
                                unfocusedBorderColor = Color(0xFF38BDF8).copy(alpha = 0.2f),
                                focusedTextColor = Color(0xFFF8FAFC),
                                unfocusedTextColor = Color(0xFFF8FAFC),
                                focusedLabelColor = Color(0xFF38BDF8)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = variablesInput,
                            onValueChange = { variablesInput = it },
                            label = { Text("Variables Description") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF38BDF8),
                                unfocusedBorderColor = Color(0xFF38BDF8).copy(alpha = 0.2f),
                                focusedTextColor = Color(0xFFF8FAFC),
                                unfocusedTextColor = Color(0xFFF8FAFC),
                                focusedLabelColor = Color(0xFF38BDF8)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (titleInput.isNotBlank() && formulaInput.isNotBlank()) {
                                viewModel.insertFormula(
                                    title = titleInput,
                                    category = categoryInput.ifBlank { "General" },
                                    formula = formulaInput,
                                    variablesString = variablesInput
                                )
                                titleInput = ""
                                categoryInput = ""
                                formulaInput = ""
                                variablesInput = ""
                                showAddDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8), contentColor = Color(0xFF090D16)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel", color = Color(0xFF94A3B8))
                    }
                }
            )
        }
    }
}
