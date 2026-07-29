package com.example.luminarcalculator.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.luminarcalculator.data.CalculationEntity
import com.example.luminarcalculator.data.CalculatorRepository
import com.example.luminarcalculator.data.CalculatorDatabase
import com.example.luminarcalculator.data.FormulaEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CalculatorRepository

    val allCalculations: Flow<List<CalculationEntity>>
    val allFormulas: Flow<List<FormulaEntity>>

    var currentExpression by mutableStateOf("")
        private set

    var calculationResult by mutableStateOf("")
        private set

    var memoryValue by mutableStateOf(0.0)
        private set

    init {
        val calculationDao = CalculatorDatabase.getDatabase(application).calculationDao()
        repository = CalculatorRepository(calculationDao)
        allCalculations = repository.allCalculations
        allFormulas = emptyFlow()
    }

    fun onAction(action: String) {
        when (action) {
            "C" -> clear()
            "⌫" -> backspace()
            "=" -> evaluate()
            else -> append(action)
        }
    }

    private fun clear() {
        currentExpression = ""
        calculationResult = ""
    }

    private fun backspace() {
        if (currentExpression.isNotEmpty()) {
            currentExpression = currentExpression.dropLast(1)
        }
    }

    private fun append(value: String) {
        currentExpression += value
    }

    private fun evaluate() {
        if (currentExpression.isBlank()) return
        try {
            val result = evaluateExpression(currentExpression)
            calculationResult = if (result == result.toLong().toDouble()) {
                result.toLong().toString()
            } else {
                result.toString()
            }
            
            viewModelScope.launch {
                repository.insertCalculation(
                    CalculationEntity(
                        0,
                        currentExpression,
                        calculationResult,
                        System.currentTimeMillis()
                    )
                )
            }
        } catch (e: Exception) {
            calculationResult = "Error"
        }
    }

    private fun evaluateExpression(expr: String): Double {
        val formatted = expr.replace("×", "*").replace("÷", "/")
        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < formatted.length) formatted[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < formatted.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.code)) x += parseTerm()
                    else if (eat('-'.code)) x -= parseTerm()
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.code)) x *= parseFactor()
                    else if (eat('/'.code)) x /= parseFactor()
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return -parseFactor()

                var x: Double
                val startPos = pos
                if (eat('('.code)) {
                    x = parseExpression()
                    eat(')'.code)
                } else if ((ch >= '0'.code && ch <= '9'.code) || ch == '.'.code) {
                    while ((ch >= '0'.code && ch <= '9'.code) || ch == '.'.code) nextChar()
                    x = formatted.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
                return x
            }
        }.parse()
    }

    fun clearHistory() = viewModelScope.launch {
        repository.clearHistory()
    }

    fun insertFormula(title: String, category: String, formula: String, variablesString: String) {}

    fun deleteFormula(formula: FormulaEntity) {}
}
