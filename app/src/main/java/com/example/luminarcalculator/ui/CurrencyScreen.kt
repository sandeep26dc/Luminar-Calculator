package com.example.luminarcalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

data class CurrencyRateItem(
    val currencyCode: String,
    val currencyName: String,
    val symbol: String,
    val liveRate: Double,
    val thumbRuleRate: Double,
    val isOnlineRate: Boolean
)

@Composable
fun CurrencyScreen(isDarkMode: Boolean) {
    var baseCurrency by rememberSaveable { mutableStateOf("USD") }
    var inputValue by rememberSaveable { mutableStateOf("100") }
    var isOnline by remember { mutableStateOf(false) }
    var ratesMap by remember { mutableStateOf<Map<String, Double>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(false) }
    
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val currencies = listOf("USD", "EUR", "GBP", "AED", "INR", "SAR", "AUD", "CAD")

    // Standard engineering / fallback thumb-rule conversion multipliers relative to USD
    val thumbRuleMap = mapOf(
        "USD" to 1.0,
        "EUR" to 0.92,
        "GBP" to 0.79,
        "AED" to 3.67,
        "INR" to 83.2,
        "SAR" to 3.75,
        "AUD" to 1.52,
        "CAD" to 1.36
    )

    val currencyNames = mapOf(
        "USD" to "US Dollar",
        "EUR" to "Euro",
        "GBP" to "British Pound",
        "AED" to "UAE Dirham",
        "INR" to "Indian Rupee",
        "SAR" to "Saudi Riyal",
        "AUD" to "Australian Dollar",
        "CAD" to "Canadian Dollar"
    )

    val currencySymbols = mapOf(
        "USD" to "$",
        "EUR" to "€",
        "GBP" to "£",
        "AED" to "AED",
        "INR" to "₹",
        "SAR" to "SAR",
        "AUD" to "A$",
        "CAD" to "C$"
    )

    // Function to fetch live rates via open API when connected
    fun fetchLiveRates() {
        coroutineScope.launch {
            isLoading = true
            val fetchedData = withContext(Dispatchers.IO) {
                try {
                    val url = URL("https://open.er-api.com/v6/latest/$baseCurrency")
                    val connection = url.openConnection()
                    connection.connectTimeout = 3000
                    connection.readTimeout = 3000
                    val response = url.readText()
                    val json = JSONObject(response)
                    if (json.getString("result") == "success") {
                        val ratesObj = json.getJSONObject("rates")
                        val map = mutableMapOf<String, Double>()
                        val keys = ratesObj.keys()
                        while (keys.hasNext()) {
                            val key = keys.next()
                            map[key] = ratesObj.getDouble(key)
                        }
                        map
                    } else null
                } catch (e: Exception) {
                    null
                }
            }

            if (fetchedData != null) {
                ratesMap = fetchedData
                isOnline = true
            } else {
                isOnline = false
            }
            isLoading = false
        }
    }

    LaunchedEffect(baseCurrency) {
        fetchLiveRates()
    }

    val parsedInput = inputValue.toDoubleOrNull() ?: 0.0

    val displayList = currencies.filter { it != baseCurrency }.map { code ->
        val liveRate = ratesMap[code] ?: thumbRuleMap[code] ?: 1.0
        val thumbRate = thumbRuleMap[code] ?: 1.0
        val activeRate = if (isOnline && ratesMap.containsKey(code)) liveRate else thumbRate
        CurrencyRateItem(
            currencyCode = code,
            currencyName = currencyNames[code] ?: code,
            symbol = currencySymbols[code] ?: code,
            liveRate = parsedInput * activeRate,
            thumbRuleRate = parsedInput * thumbRate,
            isOnlineRate = isOnline
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
    ) {
        // Header Status Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            if (isOnline) Color(0xFF22C55E) else Color(0xFFF59E0B),
                            shape = RoundedCornerShape(50)
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isOnline) "LIVE RATES ONLINE" else "OFFLINE THUMB-RULE MODE",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
            }

            IconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    fetchLiveRates()
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh Rates",
                    tint = Color(0xFF38BDF8)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Base Currency Selector Tabs
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
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                currencies.take(4).forEach { cur ->
                    val isSelected = baseCurrency == cur
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            baseCurrency = cur
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
                            text = cur,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Input Field
        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { 
                Text(
                    text = "ENTER AMOUNT ($baseCurrency)", 
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

        Spacer(modifier = Modifier.height(20.dp))

        // Conversion Results List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(displayList) { item ->
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
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = item.currencyCode,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF8FAFC)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.currencyName,
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${item.symbol} ${String.format("%.2f", item.liveRate)}",
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            if (!item.isOnlineRate) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Thumb-rule rate",
                                    fontSize = 10.sp,
                                    color = Color(0xFFF59E0B)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
