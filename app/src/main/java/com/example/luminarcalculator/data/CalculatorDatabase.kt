package com.example.luminarcalculator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CalculationEntity::class, FormulaEntity::class], version = 2, exportSchema = false)
abstract class CalculatorDatabase : RoomDatabase() {
    abstract fun calculationDao(): CalculationDao

    companion object {
        @Volatile
        private var INSTANCE: CalculatorDatabase? = null

        fun getDatabase(context: Context): CalculatorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CalculatorDatabase::class.java,
                    "luminar_calculator_database"
                )
                    .fallbackToDestructiveMigration() // Safely handles version bump for development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
