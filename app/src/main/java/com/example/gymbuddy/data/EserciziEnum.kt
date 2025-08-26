package com.example.gymbuddy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EserciziEnum (
    @PrimaryKey
    val nome: String,
)
