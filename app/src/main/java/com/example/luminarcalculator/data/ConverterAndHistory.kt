package com.example.luminarcalculator.data

data class ConversionResult(val unitName: String, val value: Double)

data class CurrencyComparisonResult(
    val currencyCode: String,
    val standardRate: Double,
    val liveMarketRate: Double,
    val isOnline: Boolean
) {
    val variancePercentage: Double
        get() = if (standardRate > 0.0) {
            ((liveMarketRate - standardRate) / standardRate) * 100.0
        } else 0.0

    val marketStatus: String
        get() = when {
            !isOnline -> "Offline Mode (Using Standard Rate)"
            variancePercentage > 0.2 -> "Inflated by ${String.format("%.2f", variancePercentage)}% vs Standard"
            variancePercentage < -0.2 -> "Deflated by ${String.format("%.2f", kotlin.math.abs(variancePercentage))}% vs Standard"
            else -> "Aligned with Standard Baseline"
        }
}

object ConverterAndHistory {
    fun convert(category: String, input: Double): List<ConversionResult> {
        return when (category) {
            "Length" -> listOf(
                ConversionResult("Meters (m)", input),
                ConversionResult("Kilometers (km)", input / 1000.0),
                ConversionResult("Feet (ft)", input * 3.28084),
                ConversionResult("Miles (mi)", input * 0.000621371)
            )
            "Weight" -> listOf(
                ConversionResult("Kilograms (kg)", input),
                ConversionResult("Grams (g)", input * 1000.0),
                ConversionResult("Pounds (lbs)", input * 2.20462),
                ConversionResult("Ounces (oz)", input * 35.274)
            )
            "Temp" -> listOf(
                ConversionResult("Celsius (°C)", input),
                ConversionResult("Fahrenheit (°F)", (input * 9 / 5) + 32),
                ConversionResult("Kelvin (K)", input + 273.15)
            )
            else -> emptyList()
        }
    }

    // Standard vs Live Market Currency Comparison with Inflation/Deflation tracking
    fun compareCurrencyRate(
        currencyCode: String,
        standardBaselineRate: Double,
        liveOnlineRate: Double,
        isOnline: Boolean
    ): CurrencyComparisonResult {
        return CurrencyComparisonResult(
            currencyCode = currencyCode,
            standardRate = standardBaselineRate,
            liveMarketRate = liveOnlineRate,
            isOnline = isOnline
        )
    }
}
