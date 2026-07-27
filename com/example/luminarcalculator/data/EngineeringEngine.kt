package com.example.luminarcalculator.data

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

sealed class EngineeringCategory {
    data class Civil(
        val lengthMeters: BigDecimal,
        val widthMeters: BigDecimal,
        val depthMeters: BigDecimal
    ) : EngineeringCategory()

    data class MechanicalPipe(
        val outerDiameterMm: BigDecimal,
        val wallThicknessMm: BigDecimal,
        val lengthMeters: BigDecimal,
        val materialDensityKgM3: BigDecimal = BigDecimal("7850") // Default Steel
    ) : EngineeringCategory()

    data class ElectricalOhm(
        val voltageV: BigDecimal?,
        val currentA: BigDecimal?,
        val resistanceOhm: BigDecimal?
    ) : EngineeringCategory()
}

data class EngineeringResult(
    val title: String,
    val primaryMetric: String,
    val secondaryMetrics: Map<String, String>
)

object EngineeringEngine {

    private val mathContext = MathContext(10, RoundingMode.HALF_UP)

    fun calculate(category: EngineeringCategory): EngineeringResult {
        return when (category) {
            is Civil -> {
                // Concrete Volume & Surface Area
                val volume = category.lengthMeters
                    .multiply(category.widthMeters)
                    .multiply(category.depthMeters, mathContext)
                
                val wetVolume = volume.multiply(BigDecimal("1.03"), mathContext) // 3% waste contingency
                
                EngineeringResult(
                    title = "Civil Estimation (Concrete)",
                    primaryMetric = "${volume.stripTrailingZeros().toPlainString()} m³",
                    secondaryMetrics = mapOf(
                        "With 3% Waste" to "${wetVolume.stripTrailingZeros().toPlainString()} m³",
                        "Footprint Area" to "${category.lengthMeters.multiply(category.widthMeters).stripTrailingZeros().toPlainString()} m²"
                    )
                )
            }
            is MechanicalPipe -> {
                // Pipe Weight & Volume
                // ID = OD - (2 * thickness)
                val twoTimesT = category.wallThicknessMm.multiply(BigDecimal("2"))
                val innerDiameterMm = category.outerDiameterMm.subtract(twoTimesT)
                
                // Cross sectional area in mm² -> convert to m² (divide by 1,000,000)
                // Area = (PI / 4) * (OD² - ID²)
                val pi = BigDecimal(Math.PI)
                val odSq = category.outerDiameterMm.pow(2, mathContext)
                val idSq = if (innerDiameterMm.compareTo(BigDecimal.ZERO) > 0) innerDiameterMm.pow(2, mathContext) else BigDecimal.ZERO
                
                val areaMm2 = pi.divide(BigDecimal("4"), mathContext).multiply(odSq.subtract(idSq))
                val areaM2 = areaMm2.divide(BigDecimal("1000000"), mathContext)
                
                val volumeM3 = areaM2.multiply(category.lengthMeters, mathContext)
                val totalWeightKg = volumeM3.multiply(category.materialDensityKgM3, mathContext)
                val weightPerMeter = if (category.lengthMeters.compareTo(BigDecimal.ZERO) > 0) {
                    totalWeightKg.divide(category.lengthMeters, mathContext)
                } else BigDecimal.ZERO

                EngineeringResult(
                    title = "Pipe & Structural Weight",
                    primaryMetric = "${totalWeightKg.setScale(2, RoundingMode.HALF_UP)} kg",
                    secondaryMetrics = mapOf(
                        "Weight per Meter" to "${weightPerMeter.setScale(2, RoundingMode.HALF_UP)} kg/m",
                        "Internal Volume" to "${volumeM3.setScale(4, RoundingMode.HALF_UP)} m³",
                        "Inner Diameter" to "${innerDiameterMm.stripTrailingZeros().toPlainString()} mm"
                    )
                )
            }
            is ElectricalOhm -> {
                // Ohm's Law: V = I * R, I = V / R, R = V / I, P = V * I
                var v = category.voltageV
                var i = category.currentA
                var r = category.resistanceOhm
                var p = BigDecimal.ZERO

                try {
                    when {
                        v != null && i != null -> {
                            r = v.divide(i, mathContext)
                            p = v.multiply(i, mathContext)
                        }
                        v != null && r != null -> {
                            i = v.divide(r, mathContext)
                            p = v.multiply(i, mathContext)
                        }
                        i != null && r != null -> {
                            v = i.multiply(r, mathContext)
                            p = v.multiply(i, mathContext)
                        }
                    }
                } catch (e: Exception) {
                    // Division by zero safeguard
                }

                EngineeringResult(
                    title = "Electrical Matrix (Ohm's Law)",
                    primaryMetric = "${v?.stripTrailingZeros()?.toPlainString() ?: "0"} V",
                    secondaryMetrics = mapOf(
                        "Current (I)" to "${i?.stripTrailingZeros()?.toPlainString() ?: "0"} A",
                        "Resistance (R)" to "${r?.stripTrailingZeros()?.toPlainString() ?: "0"} Ω",
                        "Power (P)" to "${p?.stripTrailingZeros()?.toPlainString() ?: "0"} W"
                    )
                )
            }
        }
    }
}
