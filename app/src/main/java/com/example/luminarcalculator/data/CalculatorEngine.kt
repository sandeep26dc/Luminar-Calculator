package com.example.luminarcalculator.data

import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.*

object CalculatorEngine {

    var isDegreeMode: Boolean = true

    fun evaluate(expression: String): String {
        return try {
            val formattedExpr = prepareExpression(expression)
            val expr = ExpressionBuilder(formattedExpr).build()
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

    // Calculates Y value for a given X coordinate on the Graph Canvas
    fun evaluateForGraph(expression: String, xVal: Double): Double? {
        return try {
            val formattedExpr = prepareExpression(expression)
            val expr = ExpressionBuilder(formattedExpr)
                .variable("x")
                .build()
                .setVariable("x", xVal)

            val result = expr.evaluate()
            if (result.isNaN() || result.isInfinite()) null else result
        } catch (e: Exception) {
            null
        }
    }

    private fun prepareExpression(raw: String): String {
        var expr = raw
            .replace("×", "*")
            .replace("÷", "/")
            .replace("•", ".")
            .replace("π", "pi")
            .replace("e", "E")

        // Handle implicit multiplication like 2x -> 2*x or 2sin -> 2*sin
        expr = expr.replace(Regex("(\\d)([a-zA-Z(])"), "$1*$2")

        // Degree vs Radian conversion for Trig functions
        if (isDegreeMode) {
            expr = expr.replace(Regex("sin\\(([^)]+)\\)")) { "sin(($1)*pi/180)" }
                .replace(Regex("cos\\(([^)]+)\\)")) { "cos(($1)*pi/180)" }
                .replace(Regex("tan\\(([^)]+)\\)")) { "tan(($1)*pi/180)" }
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
