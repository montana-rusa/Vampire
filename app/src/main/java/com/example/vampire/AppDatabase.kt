package com.example.vampire

import androidx.room.*

@Database(entities=[Buy::class,Budget::class],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun buyDao() : BuyDao
    abstract fun budgetDao() : BudgetDao
}