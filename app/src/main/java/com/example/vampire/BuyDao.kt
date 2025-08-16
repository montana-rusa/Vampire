package com.example.vampire

import androidx.room.*

@Dao
interface BuyDao {

    @Query("SELECT * FROM Buy")
    fun getAll(): List<Buy>

    @Insert
    fun insertAll(vararg buys : Buy)

    @Query("DELETE FROM Buy")
    fun clearTable()

}