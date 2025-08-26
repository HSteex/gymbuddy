package com.example.gymbuddy.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Scheda::class,
            parentColumns = ["ID"],
            childColumns = ["ID_scheda"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EserciziEnum::class,
            parentColumns = ["nome"],
            childColumns = ["nome"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Esercizi (
    @PrimaryKey(autoGenerate = true)
    val ID: Int=0,
    val ID_scheda: Int,
    val esercizio: Boolean,
    val nome: String,
    val ripetizioni: Int,
    val pausa: Boolean,
    val durata: Int,
    val ordine: Int,
)