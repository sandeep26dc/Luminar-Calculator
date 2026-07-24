package com.example.luminarcalculator.data

data class ConversionResult(val unitName: String, val value: Double)

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
