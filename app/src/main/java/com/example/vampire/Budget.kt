package com.example.vampire

import androidx.room.*

@Entity
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type : String,
    val amount : Float
)
