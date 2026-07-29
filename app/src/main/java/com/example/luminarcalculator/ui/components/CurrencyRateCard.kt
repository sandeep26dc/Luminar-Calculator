package com.example.luminarcalculator.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminarcalculator.data.CurrencyRateComparison

@Composable
fun CurrencyRateCard(
    comparison: CurrencyRateComparison,
    modifier: Modifier = Modifier
) {
    val isInflated = comparison.variancePercentage > 0.5
    val isDeflated = comparison.variancePercentage < -0.5
    
    val badgeColor = when {
        !comparison.isConnected -> Color(0xFF64748B) // Slate for offline
        isInflated -> Color(0xFFEF4444)             // Red for inflation
        isDeflated -> Color(0xFF10B981)             // Green for deflation/savings
        else -> Color(0xFF38BDF8)                   // Cyan for aligned
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF1E293B).copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Currency Code & Connection/Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CURRENCY MARKET ANALYSIS (${comparison.currencyCode})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF94A3B8),
                    letterSpacing = 1.sp
                )
                
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeColor.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = if (comparison.isConnected) "LIVE" else "OFFLINE",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rates Breakdown Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Standard Rate Column
                Column {
                    Text(
                        text = "Standard Baseline",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = String.format("%.4f", comparison.standardRate),
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF8FAFC)
                    )
                }

                // Current Market Rate Column
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Live Market Rate",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (comparison.isConnected) String.format("%.4f", comparison.liveMarketRate) else "N/A",
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFF334155), thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Variance / Inflation Status Note
            Text(
                text = comparison.marketStatus,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = badgeColor
            )
        }
    }
}
