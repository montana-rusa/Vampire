package com.example.vampire

import androidx.room.*

@Entity
data class Buy(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val day : String,
    val type : String,
    val amount : Float
)
