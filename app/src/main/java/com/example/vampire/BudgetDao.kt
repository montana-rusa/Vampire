package com.example.vampire

import androidx.room.*

@Dao
interface BudgetDao {

    @Update
    fun updateBudget(budget: Budget)

    @Insert
    fun insertAll(vararg budgets : Budget)

    @Query("DELETE FROM Budget")
    fun clearTable()

    @Query("SELECT COUNT(*) FROM Budget")
    fun getRowCount(): Int

}