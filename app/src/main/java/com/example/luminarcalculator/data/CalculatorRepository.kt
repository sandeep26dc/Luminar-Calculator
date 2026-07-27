package com.example.luminarcalculator.data

import kotlinx.coroutines.flow.Flow

class CalculatorRepository(private val calculationDao: CalculationDao) {

    val allCalculations: Flow<List<CalculationEntity>> = calculationDao.getAllCalculations()

    suspend fun insertCalculation(expression: String, result: String) {
        val entity = CalculationEntity(expression = expression, result = result, timestamp = System.currentTimeMillis())
        calculationDao.insert(entity)
    }

    suspend fun clearHistory() {
        calculationDao.clearAll()
    }
}
