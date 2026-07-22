package com.example.luminarcalculator.data

data class HistoryItem(
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)

object UnitConverter {
    fun convertLength(value: Double, from: String, to: String): Double {
        val meters = when (from) {
            "Meters" -> value
            "Kilometers" -> value * 1000.0
            "Centimeters" -> value / 100.0
            "Feet" -> value * 0.3048
            "Inches" -> value * 0.0254
            else -> value
        }
        return when (to) {
            "Meters" -> meters
            "Kilometers" -> meters / 1000.0
            "Centimeters" -> meters * 100.0
            "Feet" -> meters / 0.3048
            "Inches" -> meters / 0.0254
            else -> meters
        }
    }

    fun convertMass(value: Double, from: String, to: String): Double {
        val kg = when (from) {
            "Kilograms" -> value
            "Grams" -> value / 1000.0
            "Pounds" -> value * 0.453592
            "Ounces" -> value * 0.0283495
            else -> value
        }
        return when (to) {
            "Kilograms" -> kg
            "Grams" -> kg * 1000.0
            "Pounds" -> kg / 0.453592
            "Ounces" -> kg / 0.0283495
            else -> kg
        }
    }

    fun convertTemperature(value: Double, from: String, to: String): Double {
        if (from == to) return value
        val celsius = when (from) {
            "Celsius" -> value
            "Fahrenheit" -> (value - 32) * 5 / 9
            "Kelvin" -> value - 273.15
            else -> value
        }
        return when (to) {
            "Celsius" -> celsius
            "Fahrenheit" -> (celsius * 9 / 5) + 32
            "Kelvin" -> celsius + 273.15
            else -> celsius
        }
    }
}
