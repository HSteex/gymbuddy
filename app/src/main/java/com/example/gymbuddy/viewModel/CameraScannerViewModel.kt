package com.example.gymbuddy.viewModel

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.Esercizi
import com.example.gymbuddy.data.GymRepository
import com.example.gymbuddy.data.Scheda
import com.example.gymbuddy.data.SchedaExport
import com.example.gymbuddy.ungzip
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraScannerViewModel(private val gymRepository: GymRepository): ViewModel(){

    private val _cameraScannerUiState : MutableStateFlow<CameraScannerUiState> = MutableStateFlow(CameraScannerUiState())
    val cameraScannerUiState : StateFlow<CameraScannerUiState> = _cameraScannerUiState.asStateFlow()

    private lateinit var  schedaExport: SchedaExport



    data class CameraScannerUiState(
        val titolo: String = "",
        val descrizione: String = "",
        val numeroEsercizi: Int= 0,
        val error: Boolean = false,
        val trovato: Boolean = false,
    )


    fun readSchedaFromQr(gzip: String){

        val gson= Gson()
        var json=""
        try{
            json= ungzip(Base64.decode(gzip, Base64.DEFAULT))
            schedaExport= gson.fromJson(json, SchedaExport::class.java)
            _cameraScannerUiState.value = _cameraScannerUiState.value.copy(
                titolo = schedaExport.scheda.titolo,
                descrizione = schedaExport.scheda.descrizione,
                numeroEsercizi = schedaExport.esercizi.size,
                trovato=true,
                )
        }
        catch (e: Exception){
            Log.v("json", e.toString())
            _cameraScannerUiState.value = _cameraScannerUiState.value.copy(
                error = true,
            )
        }
    }

    fun insertSchedaExport(){
        val scheda: Scheda = schedaExport.scheda.copy(ultimoAllenamento = "mai", ID = 0)
        viewModelScope.launch {
            val idScheda = gymRepository.insertScheda(scheda)
            Log.v("json", idScheda.toString())
            val esercizi = schedaExport.esercizi.map { it.copy(ID_scheda = idScheda.toInt(), ID=0) }
            Log.v("json", esercizi.toString())
            gymRepository.insertEsercizi(esercizi)
        }
    }
}