package com.example.luminarcalculator.data

enum class EngineeringCategory {
    ELECTRICAL, MECHANICAL, CIVIL, CONVERSION
}

data class EngineeringResult(
    val title: String,
    val outputValue: String,
    val description: String
)

object EngineeringEngine {
    fun calculate(category: EngineeringCategory, param1: Double, param2: Double): EngineeringResult {
        return when (category) {
            EngineeringCategory.ELECTRICAL -> EngineeringResult(
                title = "Ohm's Law / Power",
                outputValue = "${param1 * param2} Watts",
                description = "Calculated based on Voltage and Current parameters."
            )
            EngineeringCategory.MECHANICAL -> EngineeringResult(
                title = "Kinetic / Force Energy",
                outputValue = "${0.5 * param1 * (param2 * param2)} Joules",
                description = "Computed using mass and velocity inputs."
            )
            EngineeringCategory.CIVIL -> EngineeringResult(
                title = "Structural Stress",
                outputValue = "${param1 / (param2.coerceAtLeast(0.001))} Pascals",
                description = "Force divided by cross-sectional area."
            )
            EngineeringCategory.CONVERSION -> EngineeringResult(
                title = "Unit Scaling",
                outputValue = "${param1 * param2}",
                description = "Applied custom engineering conversion factor."
            )
        }
    }
}
