package com.example.luminarcalculator.data

import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorEngine {
    fun evaluate(expression: String): String {
        return try {
            val cleanExpr = expression
                .replace("×", "*")
                .replace("÷", "/")
                .replace(":", "/")

            val eval = ExpressionBuilder(cleanExpr).build().evaluate()
            
            if (eval % 1 == 0.0) {
                eval.toLong().toString()
            } else {
                eval.toString()
            }
        } catch (e: Exception) {
            "Error"
        }
    }
}
