package com.example.luminarcalculator.ui

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.AIInsightResult
import com.example.luminarcalculator.data.AIAssistantEngine

@Composable
fun AIAssistantSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    var userPrompt by remember { mutableStateOf("") }
    var resultState by remember { mutableStateOf<AIInsightResult?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFF6366F1))))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color(0xFF38BDF8))
                        )
                        Text(
                            text = "LUMINAR INTELLIGENCE AI",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    }
                    TextButton(onClick = onDismiss) {
                        Text(text = "Close", color = Color(0xFF94A3B8))
                    }
                }

                Text(
                    text = "Ask anything in natural language. Luminar will analyze formulas, variables, and structural metrics instantly.",
                    color = Color(0xFF94A3B8),
                    fontSize = 13.sp
                )

                // Input Field
                OutlinedTextField(
                    value = userPrompt,
                    onValueChange = { userPrompt = it },
                    placeholder = { Text("e.g., How much paint for a 5m x 4m wall?", color = Color(0xFF475569)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        if (userPrompt.isNotBlank()) {
                            isAnalyzing = true
                            resultState = AIAssistantEngine.processNaturalQuery(userPrompt)
                            isAnalyzing = false
                        }
                    }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF38BDF8),
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                // Quick Suggestion Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SuggestionChip(text = "Paint Volume") {
                        userPrompt = "How much paint for a 5m × 4m wall?"
                        resultState = AIAssistantEngine.processNaturalQuery(userPrompt)
                    }
                    SuggestionChip(text = "Pipe Weight") {
                        userPrompt = "Calculate carbon steel pipe weight OD 219.1mm length 12m"
                        resultState = AIAssistantEngine.processNaturalQuery(userPrompt)
                    }
                }

                // Result Box Display
                AnimatedVisibility(
                    visible = resultState != null,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut()
                ) {
                    resultState?.let { res ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF1E293B))
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = res.category.uppercase(), color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(text = res.formulaUsed, color = Color(0xFF94A3B8), fontSize = 11.sp)
                            }

                            Text(
                                text = res.primaryAnswer,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            HorizontalDivider(color = Color(0xFF334155))

                            res.breakdown.forEach { (label, valStr) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = label, color = Color(0xFF94A3B8), fontSize = 13.sp)
                                    Text(text = valStr, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestionChip(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1E293B))
            .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text = text, color = Color(0xFF38BDF8), fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
