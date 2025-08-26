package com.example.vampire

import androidx.room.*

@Entity
data class Budget(
    @PrimaryKey val id: Int,
    val type : String,
    val amount : Float
)
