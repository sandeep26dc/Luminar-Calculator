package com.example.luminarcalculator.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.CalculationEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorySheet(
    historyList: List<CalculationEntity>,
    onClearHistory: () -> Unit,
    onItemClick: (String) -> Unit = {},
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0F172A),
        tonalElevation = 8.dp,
        modifier = Modifier.border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.2f), RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Sheet Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CALCULATION HISTORY",
                    color = Color(0xFFF8FAFC),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                TextButton(onClick = onClearHistory) {
                    Text(
                        text = "Clear All",
                        color = Color(0xFF38BDF8),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (historyList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No history recorded yet",
                        color = Color(0xFF94A3B8),
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(historyList) { item ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(3.dp, RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                                .clickable {
                                    onItemClick(item.result)
                                    onDismiss()
                                },
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF1E293B).copy(alpha = 0.4f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = item.expression,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF94A3B8)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "= ${item.result}",
                                    fontSize = 22.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF38BDF8)
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
