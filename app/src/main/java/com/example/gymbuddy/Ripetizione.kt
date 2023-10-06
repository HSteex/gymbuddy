package com.example.gymbuddy

class Ripetizione (val tipologia : TipologiaEsercizio, val tempoRecupero: Int?, val numeroSerie: Int?, val peso : Float?) {

}

enum class TipologiaEsercizio (){
    SERIE,
    RECUPERO,

}