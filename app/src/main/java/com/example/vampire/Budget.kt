package com.example.vampire

import androidx.room.*

data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type : String,
    val amount : Float
)
