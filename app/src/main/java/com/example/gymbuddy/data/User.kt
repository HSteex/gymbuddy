package com.example.gymbuddy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val username: String,
    val password: String,
    val nome: String,
    val cognome: String,
)