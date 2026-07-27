package com.example.luminarcalculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.luminarcalculator.data.CalculationEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorySheet(
    historyList: List<CalculationEntity>,
    onClearHistory: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Calculation History", style = MaterialTheme.typography.titleLarge)
                TextButton(onClick = onClearHistory) {
                    Text("Clear All")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(historyList) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = item.expression, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "= ${item.result}", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}
