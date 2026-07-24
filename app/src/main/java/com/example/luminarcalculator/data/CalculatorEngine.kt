package com.example.luminarcalculator.data

data class CalculationState(val display: String, val expression: String)

object CalculatorEngine {
    fun handleInput(symbol: String, currentDisplay: String, currentExpression: String): CalculationState {
        return when (symbol) {
            "C" -> CalculationState("0", "")
            "=" -> {
                try {
                    val evaluated = evaluateExpression(currentExpression + currentDisplay)
                    CalculationState(evaluated, "$currentExpression$currentDisplay =")
                } catch (e: Exception) {
                    CalculationState("Error", "")
                }
            }
            "+", "−", "×", "÷" -> {
                val op = if (symbol == "−") "-" else if (symbol == "×") "*" else if (symbol == "÷") "/" else "+"
                CalculationState("0", "$currentExpression $currentDisplay $op ")
            }
            "%" -> {
                val valNum = currentDisplay.toDoubleOrNull() ?: 0.0
                CalculationState((valNum / 100.0).toString(), currentExpression)
            }
            "+/−" -> {
                if (currentDisplay.startsWith("-")) {
                    CalculationState(currentDisplay.removePrefix("-"), currentExpression)
                } else if (currentDisplay != "0") {
                    CalculationState("-$currentDisplay", currentExpression)
                } else {
                    CalculationState(currentDisplay, currentExpression)
                }
            }
            else -> {
                val newDisp = if (currentDisplay == "0") symbol else currentDisplay + symbol
                CalculationState(newDisp, currentExpression)
            }
        }
    }

    private fun evaluateExpression(expr: String): String {
        val cleanExpr = expr.replace(" ", "")
        return try {
            val parts = cleanExpr.split(Regex("(?<=[+\\-*/])|(?=[+\\-*/])"))
            if (parts.size >= 3) {
                var result = parts[0].toDouble()
                var i = 1
                while (i < parts.size - 1) {
                    val op = parts[i]
                    val next = parts[i + 1].toDouble()
                    when (op) {
                        "+" -> result += next
                        "-" -> result -= next
                        "*" -> result *= next
                        "/" -> result /= next
                    }
                    i += 2
                }
                if (result % 1.0 == 0.0) result.toLong().toString() else result.toString()
            } else {
                cleanExpr
            }
        } catch (e: Exception) {
            "Error"
        }
    }
}
