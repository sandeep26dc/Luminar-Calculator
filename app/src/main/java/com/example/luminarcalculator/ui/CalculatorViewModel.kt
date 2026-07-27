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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CalculatorRepository

    val allCalculations: Flow<List<CalculationEntity>>

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
            calculationResult = result.toString()
            
            viewModelScope.launch {
                repository.insertCalculation(currentExpression, calculationResult)
            }
        } catch (e: Exception) {
            calculationResult = "Error"
        }
    }

    private fun evaluateExpression(expr: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < expr.length) expr[pos].toInt() else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.toInt()) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expr.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.toInt())) x += parseTerm()
                    else if (eat('-'.toInt())) x -= parseTerm()
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.toInt())) x *= parseFactor()
                    else if (eat('/'.toInt())) x /= parseFactor()
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.toInt())) return parseFactor()
                if (eat('-'.toInt())) return -parseFactor()

                var x: Double
                val startPos = pos
                if (eat('('.toInt())) {
                    x = parseExpression()
                    eat(')'.toInt())
                } else if ((ch >= '0'.toInt() && ch <= '9'.toInt()) || ch == '.'.toInt()) {
                    while ((ch >= '0'.toInt() && ch <= '9'.toInt()) || ch == '.'.toInt()) nextChar()
                    x = expr.substring(startPos, pos).toDouble()
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
}
