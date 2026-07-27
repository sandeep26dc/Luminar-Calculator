package com.example.luminarcalculator.data

data class FormulaItem(
    val title: String,
    val category: String,
    val formula: String,
    val description: String,
    val variables: Map<String, String>
)

object FormulaLibraryEngine {
    val repository = listOf(
        FormulaItem(
            title = "Bernoulli's Equation",
            category = "Oil & Gas / Fluid Dynamics",
            formula = "P₁ + ½ρv₁² + ρgh₁ = P₂ + ½ρv₂² + ρgh₂",
            description = "Relates pressure, velocity, and elevation in steady, inviscid, incompressible fluid flow.",
            variables = mapOf("P" to "Static Pressure (Pa)", "ρ" to "Fluid Density (kg/m³)", "v" to "Flow Velocity (m/s)", "h" to "Elevation Height (m)")
        ),
        FormulaItem(
            title = "Pipe Hydrostatic Test Pressure",
            category = "Piping & Mechanical",
            formula = "Test Pressure = Design Pressure × 1.5",
            description = "Standard industrial threshold to verify structural integrity and leak tightness under stress.",
            variables = mapOf("Design Pressure" to "Maximum allowable operating pressure (MAOP)", "Multiplier" to "1.5x standard code factor")
        ),
        FormulaItem(
            title = "Ohm's Law & Electrical Power",
            category = "Electrical Engineering",
            formula = "V = I × R  |  P = V × I",
            description = "Fundamental relationship between voltage, current, resistance, and total power dissipation.",
            variables = mapOf("V" to "Voltage (Volts)", "I" to "Current (Amperes)", "R" to "Resistance (Ohms)", "P" to "Power (Watts)")
        ),
        FormulaItem(
            title = "Concrete Volume & Contingency",
            category = "Civil Infrastructure",
            formula = "Volume = L × W × D × 1.03",
            description = "Calculates total wet volume required for slab/foundations including a standard 3% site waste allowance.",
            variables = mapOf("L" to "Length (m)", "W" to "Width (m)", "D" to "Depth / Thickness (m)", "1.03" to "Waste contingency factor")
        )
    )

    fun search(query: String): List<FormulaItem> {
        if (query.isBlank()) return repository
        return repository.filter { 
            it.title.contains(query, ignoreCase = true) || 
            it.category.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
    }
}
