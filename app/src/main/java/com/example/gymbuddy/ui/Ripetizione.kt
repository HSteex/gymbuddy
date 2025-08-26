package com.example.gymbuddy.ui

class Ripetizione (val tipologia : TipologiaEsercizio, val tempoRecupero: Int?, val numeroSerie: Int?, val peso : Float?) {

}

enum class TipologiaEsercizio (){
    SERIE,
    RECUPERO,

}