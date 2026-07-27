package com.example.luminarcalculator.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.luminarcalculator.data.CalculationEntity
import com.example.luminarcalculator.data.CalculatorRepository
import com.example.luminarcalculator.data.LuminarDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CalculatorRepository

    val allCalculations: Flow<List<CalculationEntity>>

    init {
        val calculationDao = LuminarDatabase.getDatabase(application).calculationDao()
        repository = CalculatorRepository(calculationDao)
        allCalculations = repository.allCalculations
    }

    fun insertCalculation(expression: String, result: String) = viewModelScope.launch {
        repository.insertCalculation(expression, result)
    }

    fun clearHistory() = viewModelScope.launch {
        repository.clearHistory()
    }
}
