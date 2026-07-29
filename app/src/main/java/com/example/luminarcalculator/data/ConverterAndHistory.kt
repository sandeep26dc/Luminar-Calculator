package com.example.luminarcalculator.data

data class ConversionResult(val unitName: String, val value: Double)

data class CurrencyRateComparison(
    val currencyCode: String,
    val standardRate: Double,
    val liveMarketRate: Double,
    val isConnected: Boolean
) {
    val variancePercentage: Double
        get() = if (standardRate > 0.0) {
            ((liveMarketRate - standardRate) / standardRate) * 100.0
        } else 0.0

    val marketStatus: String
        get() = when {
            !isConnected -> "Offline (Using Standard Baseline)"
            variancePercentage > 0.5 -> "Inflated by ${String.format("%.2f", variancePercentage)}%"
            variancePercentage < -0.5 -> "Deflated by ${String.format("%.2f", kotlin.math.abs(variancePercentage))}%"
            else -> "Aligned with Standard Rate"
        }
}

data class UnitRateComparison(
    val itemCode: String,
    val itemName: String,
    val unitOfMeasurement: String, // e.g., "m³", "MT", "Nos", "Rm"
    val baselineRate: Double,       // Budgeted / Standard baseline rate
    val actualOrMarketRate: Double, // Current actual site rate or market quote
    val quantity: Double            // Total quantity required for the project
) {
    val totalBaselineCost: Double
        get() = baselineRate * quantity

    val totalActualCost: Double
        get() = actualOrMarketRate * quantity

    val costVariance: Double
        get() = totalActualCost - totalBaselineCost

    val variancePercentage: Double
        get() = if (baselineRate > 0.0) {
            ((actualOrMarketRate - baselineRate) / baselineRate) * 100.0
        } else 0.0

    val rateStatus: String
        get() = when {
            variancePercentage > 1.0 -> "Over Budget by ${String.format("%.2f", variancePercentage)}%"
            variancePercentage < -1.0 -> "Cost Saving of ${String.format("%.2f", kotlin.math.abs(variancePercentage))}%"
            else -> "Aligned with Baseline"
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
}
