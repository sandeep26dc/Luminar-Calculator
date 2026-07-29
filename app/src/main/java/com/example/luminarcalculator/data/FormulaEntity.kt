package com.example.luminarcalculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "formulas")
data class FormulaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val category: String,
    val formula: String,
    val variablesString: String, // Stored as a serialized string or comma-separated representation
    val isCustom: Boolean = true
)
