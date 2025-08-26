package com.example.gymbuddy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Scheda(
    @PrimaryKey(autoGenerate = true)
    val ID: Int=0,
    val titolo: String,
    val descrizione: String,
    val ultimoAllenamento: String,
    val numeroEsercizi: String,
    val isFavorite: Boolean = false,
)

