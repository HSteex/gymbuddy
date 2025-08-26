package com.example.gymbuddy.viewModel

import android.text.format.DateFormat
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.Esercizi
import com.example.gymbuddy.data.GymRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class StartAllenamentoViewModel(private val repository: GymRepository, private val savedStateHandle: SavedStateHandle): ViewModel() {

    private val idScheda = savedStateHandle["schedaId"] ?: 0
    private val _startAllenamentoUiState : MutableStateFlow<StartAllenamentoUiState> = MutableStateFlow(StartAllenamentoUiState())
    val startAllenamentoUiState: StateFlow<StartAllenamentoUiState> = _startAllenamentoUiState.asStateFlow()

    private val _eserciziList = MutableStateFlow<List<Esercizi>>(emptyList())
    val eserciziList = _eserciziList.asStateFlow()

    var timeLeft by mutableStateOf(0)
        private set

    var nomeEsercizio by mutableStateOf("")
        private set

    var ripetizioni by mutableStateOf(0)
        private set

    var pausa by mutableStateOf(false)
        private set

    var isPausaEnded by mutableStateOf(false)
        private set

    var isPaused by mutableStateOf(false)
        private set

    var durata by mutableStateOf(0)
        private set

    var numeroEsercizio by mutableStateOf(0)
        private set

    var prossimoEsercizio by mutableStateOf<Esercizi?>(null)
        private set

    private lateinit var esercizioAttuale : Esercizi
    private val isSkipped = false


    data class StartAllenamentoUiState(
        val timeLeft: Int = 0,
        val isPaused: Boolean = false,
        val nomeEsercizio: String = "",
        val ripetizioni: Int = 0,
        val pausa: Boolean = false,
        val isPausaEnded: Boolean = false,
        val durata: Int = 0,
        val numeroEsercizio: Int = 0,
        val prossimoEsercizio: Esercizi? = null,
        val isLoading: Boolean = true
    )


    fun getAllExercises(id: Int) = repository.getEserciziScheda(id)

 init{
        Log.v("viewModel", "init")
        Log.v("viewModel", "Get esercizi")
        val eserciziFlow = repository.getEserciziScheda(idScheda)
        if (_eserciziList.value.isEmpty()) {
            viewModelScope.launch {
                eserciziFlow.collect() { esercizi ->
                    _eserciziList.value = esercizi
                    Log.v("viewModel", esercizi.toString())
                    esercizioAttuale = this@StartAllenamentoViewModel.eserciziList.value.first()
                    if (this@StartAllenamentoViewModel.eserciziList.value.isNotEmpty()) {
                        if (esercizioAttuale.pausa) {
                            pausa = true
                            durata= esercizioAttuale.durata
                            timeLeft = esercizioAttuale.durata
                            starTimer()
                        } else {
                            nomeEsercizio = esercizioAttuale.nome
                            ripetizioni = esercizioAttuale.ripetizioni

                        }
                        prossimoEsercizio =
                            this@StartAllenamentoViewModel.eserciziList.value.get(1)
                        _startAllenamentoUiState.value = startAllenamentoUiState.value.copy(
                            nomeEsercizio = nomeEsercizio,
                            ripetizioni = ripetizioni,
                            pausa = pausa,
                            prossimoEsercizio = prossimoEsercizio,
                            durata= durata,
                            isLoading = false,
                            timeLeft = timeLeft

                        )
                    }
                }
            }
        }
    }

    fun starTimer(){

        //Lunch a coroutine
        viewModelScope.launch {
            while (timeLeft>0 && !isPaused && !isSkipped){
                //Wait 1 second
                delay(1000)
                //Decrease timeLeft by 1
                if(!isPaused){
                    timeLeft--}
                //Update the UI
                _startAllenamentoUiState.value = startAllenamentoUiState.value.copy(timeLeft = timeLeft)
            }
            if(timeLeft==0){
                _startAllenamentoUiState.value = startAllenamentoUiState.value.copy(isPausaEnded = true)
            }

        }
    }

    fun onSkip(){
        if (eserciziList.value.isNotEmpty()) {
            if (numeroEsercizio < eserciziList.value.size - 1) {
                numeroEsercizio++
                esercizioAttuale = eserciziList.value.get(numeroEsercizio)
                if (esercizioAttuale.pausa) {
                    pausa = true
                    durata= esercizioAttuale.durata
                    timeLeft = esercizioAttuale.durata
                    starTimer()
                } else {
                    pausa=false
                    timeLeft=0
                    nomeEsercizio = esercizioAttuale.nome
                    ripetizioni = esercizioAttuale.ripetizioni
                }
                if (numeroEsercizio < eserciziList.value.size - 1) {
                    prossimoEsercizio = eserciziList.value.get(numeroEsercizio + 1)
                } else {
                    prossimoEsercizio = null
                }
                _startAllenamentoUiState.value = startAllenamentoUiState.value.copy(
                    nomeEsercizio = nomeEsercizio,
                    ripetizioni = ripetizioni,
                    pausa = pausa,
                    prossimoEsercizio = prossimoEsercizio,
                    durata= durata,
                    numeroEsercizio = numeroEsercizio,
                    timeLeft = timeLeft

                )
            }
        }

    }

    fun onPlayPause(){
        isPaused = !isPaused
        _startAllenamentoUiState.value = startAllenamentoUiState.value.copy(isPaused = isPaused)
        starTimer()
    }

    suspend fun onEnd(){
        var formatted = DateFormat.format("dd/MM/yyyy", Calendar.getInstance().time).toString()
        repository.updateUltimoAllenamento(idScheda, formatted)
    }




}