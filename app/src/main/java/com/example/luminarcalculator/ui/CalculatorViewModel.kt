package com.example.luminarcalculator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luminarcalculator.data.CalculationEntity
import com.example.luminarcalculator.data.CalculationDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalculatorViewModel(
    private val calculationDao: CalculationDao? = null
) : ViewModel() {

    private val _history = MutableStateFlow<List<CalculationEntity>>(emptyList())
    val history: StateFlow<List<CalculationEntity>> = _history.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            calculationDao?.let { dao ->
                _history.value = dao.getAllCalculations()
            }
        }
    }

    fun saveCalculation(expression: String, result: String) {
        viewModelScope.launch {
            calculationDao?.insertCalculation(
                CalculationEntity(
                    expression = expression,
                    result = result
                )
            )
            loadHistory()
        }
    }
}
