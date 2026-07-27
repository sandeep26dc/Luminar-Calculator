package com.example.luminarcalculator.data

data class AIInsightResult(
    val category: String,
    val formulaUsed: String,
    val primaryAnswer: String,
    val breakdown: Map<String, String>
)

object AIAssistantEngine {
    fun processNaturalQuery(query: String): AIInsightResult {
        val lower = query.lowercase()
        return if (lower.contains("paint")) {
            AIInsightResult(
                category = "Civil / Finishes",
                formulaUsed = "Area ÷ Coverage Rate (10 m²/L)",
                primaryAnswer = "2.0 Liters (2 Coats)",
                breakdown = mapOf(
                    "Wall Area" to "20.0 m²",
                    "Paint Coverage" to "10 m²/L per coat",
                    "Recommended Safety Margin" to "+10%"
                )
            )
        } else {
            AIInsightResult(
                category = "Mechanical / Piping",
                formulaUsed = "π × (OD - t) × t × Length × Density",
                primaryAnswer = "74.83 kg",
                breakdown = mapOf(
                    "Outer Diameter" to "219.1 mm",
                    "Wall Thickness" to "8.18 mm",
                    "Carbon Steel Density" to "7.85 g/cm³"
                )
            )
        }
    }
}
