package com.example.luminarcalculator.data

import kotlinx.coroutines.flow.Flow

class CalculatorRepository(private val calculationDao: CalculationDao) {
    val allCalculations: Flow<List<CalculationEntity>> = calculationDao.getAllCalculations()

    suspend fun insert(calculation: CalculationEntity) {
        calculationDao.insertCalculation(calculation)
    }

    suspend fun clearHistory() {
        calculationDao.clearHistory()
    }
}
