package com.example.luminarcalculator.data

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

data class CalculationState(val display: String, val expression: String)

object CalculatorEngine {

    private val mathContext = MathContext(16, RoundingMode.HALF_UP)

    fun handleInput(symbol: String, currentDisplay: String, currentExpression: String): CalculationState {
        return when (symbol) {
            "C" -> CalculationState("0", "")
            "=" -> {
                try {
                    val fullExpr = if (currentExpression.endsWith("=")) currentDisplay else "$currentExpression$currentDisplay"
                    val evaluated = evaluateExpression(fullExpr)
                    CalculationState(evaluated, "$fullExpr =")
                } catch (e: Exception) {
                    CalculationState("Error", "")
                }
            }
            "+", "−", "×", "÷" -> {
                val op = when(symbol) {
                    "−" -> "-"
                    "×" -> "*"
                    "÷" -> "/"
                    else -> "+"
                }
                val base = if (currentExpression.endsWith("=")) currentDisplay else "$currentExpression $currentDisplay"
                CalculationState("0", "$base $op ")
            }
            "%" -> {
                try {
                    val bd = BigDecimal(currentDisplay).divide(BigDecimal("100"), mathContext).stripTrailingZeros()
                    CalculationState(bd.toPlainString(), currentExpression)
                } catch (e: Exception) {
                    CalculationState("Error", currentExpression)
                }
            }
            "+/−" -> {
                if (currentDisplay.startsWith("-")) {
                    CalculationState(currentDisplay.removePrefix("-"), currentExpression)
                } else if (currentDisplay != "0" && currentDisplay != "Error") {
                    CalculationState("-$currentDisplay", currentExpression)
                } else {
                    CalculationState(currentDisplay, currentExpression)
                }
            }
            else -> {
                // If the previous state was just evaluated, typing a number starts a fresh expression
                val isPostEval = currentExpression.endsWith("=")
                val baseExpr = if (isPostEval) "" else currentExpression
                val newDisp = if (currentDisplay == "0" || isPostEval) symbol else currentDisplay + symbol
                CalculationState(newDisp, baseExpr)
            }
        }
    }

    private fun evaluateExpression(expr: String): String {
        val cleanExpr = expr.replace(" ", "")
        return try {
            // Split into numbers and operators using regex tokenization
            val tokens = Regex("(?<=[+\\-*/])|(?=[+\\-*/])").split(cleanExpr).filter { it.isNotBlank() }
            
            if (tokens.isEmpty()) return "0"
            if (tokens.size == 1) return BigDecimal(tokens[0]).stripTrailingZeros().toPlainString()

            // First pass: Handle multiplication and division (Operator Precedence)
            val intermediate = mutableListOf<String>()
            var i = 0
            while (i < tokens.size) {
                val token = tokens[i]
                if ((token == "*" || token == "/") && intermediate.isNotEmpty() && i + 1 < tokens.size) {
                    val prev = BigDecimal(intermediate.removeAt(intermediate.size - 1))
                    val next = BigDecimal(tokens[i + 1])
                    val res = if (token == "*") {
                        prev.multiply(next, mathContext)
                    } else {
                        if (next.compareTo(BigDecimal.ZERO) == 0) throw ArithmeticException("Division by zero")
                        prev.divide(next, mathContext)
                    }
                    intermediate.add(res.toPlainString())
                    i += 2
                } else {
                    intermediate.add(token)
                    i++
                }
            }

            // Second pass: Handle addition and subtraction
            var result = BigDecimal(intermediate[0])
            i = 1
            while (i < intermediate.size - 1) {
                val op = intermediate[i]
                val next = BigDecimal(intermediate[i + 1])
                result = when (op) {
                    "+" -> result.add(next, mathContext)
                    "-" -> result.subtract(next, mathContext)
                    else -> result
                }
                i += 2
            }

            result.stripTrailingZeros().toPlainString()
        } catch (e: Exception) {
            "Error"
        }
    }
}
