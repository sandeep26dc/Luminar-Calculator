package com.example.luminarcalculator.data

import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.function.Function
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

enum class AngleMode { DEG, RAD }

class CalculatorEngine {

    var memoryRegister: Double = 0.0
        private set

    var angleMode: AngleMode = AngleMode.DEG

    private val numberFormatter = DecimalFormat("#,##0.########", DecimalFormatSymbols(Locale.US))

    // Custom Trig Functions respecting DEG/RAD mode
    private fun getCustomFunctions(): List<Function> {
        val isDeg = angleMode == AngleMode.DEG
        return listOf(
            object : Function("sin", 1) {
                override fun apply(vararg args: Double): Double {
                    val rad = if (isDeg) Math.toRadians(args[0]) else args[0]
                    return Math.sin(rad)
                }
            },
            object : Function("cos", 1) {
                override fun apply(vararg args: Double): Double {
                    val rad = if (isDeg) Math.toRadians(args[0]) else args[0]
                    return Math.cos(rad)
                }
            },
            object : Function("tan", 1) {
                override fun apply(vararg args: Double): Double {
                    val rad = if (isDeg) Math.toRadians(args[0]) else args[0]
                    return Math.tan(rad)
                }
            },
            object : Function("ln", 1) {
                override fun apply(vararg args: Double): Double = Math.log(args[0])
            },
            object : Function("log", 1) {
                override fun apply(vararg args: Double): Double = Math.log10(args[0])
            },
            object : Function("sqrt", 1) {
                override fun apply(vararg args: Double): Double = Math.sqrt(args[0])
            }
        )
    }

    fun evaluate(expression: String): String {
        if (expression.isBlank()) return "0"

        return try {
            val cleanExpr = expression
                .replace("×", "*")
                .replace("÷", "/")
                .replace(":", "/")
                .replace("π", "pi")
                .replace("√(", "sqrt(")
                .replace("%", "*0.01")

            val exprBuilder = ExpressionBuilder(cleanExpr)
                .functions(getCustomFunctions())
                .variables("pi", "e")
                .build()

            exprBuilder.setVariable("pi", Math.PI)
            exprBuilder.setVariable("e", Math.E)

            val eval = exprBuilder.evaluate()

            if (eval.isNaN() || eval.isInfinite()) {
                "Error"
            } else {
                formatResult(eval)
            }
        } catch (e: Exception) {
            "Error"
        }
    }

    fun formatResult(value: Double): String {
        return if (value % 1 == 0.0 && value < 1e11 && value > -1e11) {
            numberFormatter.format(value.toLong())
        } else {
            numberFormatter.format(value)
        }
    }

    // --- Memory Actions ---
    fun memoryAdd(currentResult: String) {
        val num = currentResult.replace(",", "").toDoubleOrNull() ?: 0.0
        memoryRegister += num
    }

    fun memorySubtract(currentResult: String) {
        val num = currentResult.replace(",", "").toDoubleOrNull() ?: 0.0
        memoryRegister -= num
    }

    fun memoryClear() {
        memoryRegister = 0.0
    }

    fun memoryRecall(): String {
        return formatResult(memoryRegister)
    }
}
