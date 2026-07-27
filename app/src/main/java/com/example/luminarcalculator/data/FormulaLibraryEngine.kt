package com.example.luminarcalculator.data

data class FormulaItem(
    val id: String,
    val title: String,
    val category: String,
    val expression: String,
    val description: String
)

object FormulaLibraryEngine {
    fun getFormulas(): List<FormulaItem> {
        return listOf(
            FormulaItem("1", "Quadratic Formula", "Algebra", "(-b ± √(b² - 4ac)) / 2a", "Finds roots of a quadratic equation."),
            FormulaItem("2", "Kinetic Energy", "Physics", "0.5 * m * v²", "Energy of an object in motion."),
            FormulaItem("3", "Ohm's Law", "Electrical", "V = I * R", "Relationship between voltage, current, and resistance."),
            FormulaItem("4", "Area of Circle", "Geometry", "π * r²", "Calculates space enclosed by a circle.")
        )
    }
}
