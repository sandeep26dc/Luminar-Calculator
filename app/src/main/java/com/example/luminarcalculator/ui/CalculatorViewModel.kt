package com.example.luminarcalculator.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.luminarcalculator.data.CalculationEntity
import com.example.luminarcalculator.data.CalculatorDatabase
import com.example.luminarcalculator.data.CalculatorEngine
import com.example.luminarcalculator.data.CalculatorRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CalculatorRepository

    val allCalculations: StateFlow<List<CalculationEntity>>

    var displayValue by mutableStateOf("0")
        private set

    var expressionValue by mutableStateOf("")
        private set

    init {
        val calculationDao = CalculatorDatabase.getDatabase(application).calculationDao()
        repository = CalculatorRepository(calculationDao)
        allCalculations = repository.allCalculations.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun onButtonClick(symbol: String) {
        val state = CalculatorEngine.handleInput(symbol, displayValue, expressionValue)
        displayValue = state.display
        expressionValue = state.expression

        // If an expression successfully evaluated, save it to the Room database
        if (symbol == "=" && expressionValue.isNotBlank() && displayValue != "Error") {
            viewModelScope.launch {
                repository.insertCalculation(expression = expressionValue, result = displayValue)
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
