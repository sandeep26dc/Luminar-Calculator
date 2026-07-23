package com.example.luminarcalculator.data

import net.objecthunter.exp4j.ExpressionBuilder

object CalculatorEngine {

    var isDegreeMode: Boolean = true

    fun evaluate(expression: String): String {
        if (expression.isBlank()) return ""
        return try {
            val sanitized = sanitizeExpression(expression)
            val expr = ExpressionBuilder(sanitized).build()
            val evalResult = expr.evaluate()

            if (evalResult.isNaN() || evalResult.isInfinite()) {
                "Error"
            } else {
                formatResult(evalResult)
            }
        } catch (e: Exception) {
            "Error"
        }
    }

    // Live Evaluation (returns empty on incomplete syntax so UI doesn't show errors while typing)
    fun evaluateLive(expression: String): String {
        if (expression.isBlank()) return ""
        return try {
            val sanitized = sanitizeExpression(expression)
            val expr = ExpressionBuilder(sanitized).build()
            val evalResult = expr.evaluate()

            if (evalResult.isNaN() || evalResult.isInfinite()) "" else formatResult(evalResult)
        } catch (e: Exception) {
            ""
        }
    }

    fun evaluateForGraph(expression: String, xVal: Double): Double? {
        if (expression.isBlank()) return null
        return try {
            val sanitized = sanitizeExpression(expression)
            val expr = ExpressionBuilder(sanitized)
                .variable("x")
                .build()
                .setVariable("x", xVal)

            val result = expr.evaluate()
            if (result.isNaN() || result.isInfinite()) null else result
        } catch (e: Exception) {
            null
        }
    }

    private fun sanitizeExpression(raw: String): String {
        var expr = raw
            .replace("×", "*")
            .replace("÷", "/")
            .replace("•", ".")
            .replace("π", "pi")
            .replace("e", "E")

        val openCount = expr.count { it == '(' }
        val closeCount = expr.count { it == ')' }
        if (openCount > closeCount) {
            expr += ")".repeat(openCount - closeCount)
        }

        if (isDegreeMode) {
            expr = expr.replace(Regex("sin\\(([^)]+)\\)")) { "sin((${it.groupValues[1]})*pi/180)" }
                .replace(Regex("cos\\(([^)]+)\\)")) { "cos((${it.groupValues[1]})*pi/180)" }
                .replace(Regex("tan\\(([^)]+)\\)")) { "tan((${it.groupValues[1]})*pi/180)" }
        }

        return expr
    }

    private fun formatResult(value: Double): String {
        return if (value == value.toLong().toDouble()) {
            value.toLong().toString()
        } else {
            String.format("%.6f", value).trimEnd('0').trimEnd('.')
        }
    }
}
