package com.example.luminarcalculator.data

import net.objecthunter.exp4j.ExpressionBuilder

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
            "Inches" -> meters * 0.0254
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

    fun convertArea(value: Double, from: String, to: String): Double {
        val sqMeters = when (from) {
            "Sq Meters" -> value
            "Sq Feet" -> value * 0.092903
            "Acres" -> value * 4046.86
            "Hectares" -> value * 10000.0
            else -> value
        }
        return when (to) {
            "Sq Meters" -> sqMeters
            "Sq Feet" -> sqMeters / 0.092903
            "Acres" -> sqMeters / 4046.86
            "Hectares" -> sqMeters / 10000.0
            else -> sqMeters
        }
    }

    fun convertData(value: Double, from: String, to: String): Double {
        val bytes = when (from) {
            "Bytes" -> value
            "KB" -> value * 1024.0
            "MB" -> value * 1024.0 * 1024.0
            "GB" -> value * 1024.0 * 1024.0 * 1024.0
            else -> value
        }
        return when (to) {
            "Bytes" -> bytes
            "KB" -> bytes / 1024.0
            "MB" -> bytes / (1024.0 * 1024.0)
            "GB" -> bytes / (1024.0 * 1024.0 * 1024.0)
            else -> bytes
        }
    }
}

object GraphEvaluator {
    fun evaluatePoint(exprStr: String, xVal: Double): Double? {
        return try {
            val cleanExpr = exprStr
                .replace("×", "*")
                .replace("÷", "/")

            val expr = ExpressionBuilder(cleanExpr)
                .variable("x")
                .build()
                .setVariable("x", xVal)

            val res = expr.evaluate()
            if (res.isNaN() || res.isInfinite()) null else res
        } catch (e: Exception) {
            null
        }
    }
}
