package com.example.vampire

import androidx.room.*

@Dao
interface BudgetDao {

    @Update
    fun updateBudget(budget: Budget)

    @Insert
    fun insertAll(vararg buys : Buy)

    @Query("DELETE FROM Buy")
    fun clearTable()

}