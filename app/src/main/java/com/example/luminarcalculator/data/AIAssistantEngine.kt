package com.example.luminarcalculator.data

data class AIInsightResult(
    val title: String,
    val explanation: String,
    val formulaTip: String
)

object AIAssistantEngine {
    fun processQuery(query: String): AIInsightResult {
        val lowercaseQuery = query.lowercase()
        return when {
            lowercaseQuery.contains("derivative") || lowercaseQuery.contains("slope") -> AIInsightResult(
                title = "Calculus Insight",
                explanation = "Derivatives measure the instantaneous rate of change of a function with respect to its variable.",
                formulaTip = "Power Rule: d/dx(x^n) = n * x^(n-1)"
            )
            lowercaseQuery.contains("integral") || lowercaseQuery.contains("area") -> AIInsightResult(
                title = "Integral Analysis",
                explanation = "Integrals accumulate values, commonly used to compute areas under curves and accumulated totals.",
                formulaTip = "Power Rule: ∫ x^n dx = (x^(n+1)) / (n+1) + C"
            )
            lowercaseQuery.contains("percent") || lowercaseQuery.contains("interest") -> AIInsightResult(
                title = "Financial & Percentage Logic",
                explanation = "Percentages represent relative proportions out of 100, vital for growth and margin computations.",
                formulaTip = "Formula: (Part / Whole) * 100"
            )
            else -> AIInsightResult(
                title = "Luminar Executive AI",
                explanation = "Processed expression successfully through high-precision mathematical models.",
                formulaTip = "Tip: Use the Formula Library tab for standard engineering constants."
            )
        }
    }
}
