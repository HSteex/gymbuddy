package com.example.gymbuddy.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.Esercizi
import com.example.gymbuddy.data.GymDatabase
import com.example.gymbuddy.data.GymRepository
import com.example.gymbuddy.data.OfflineGymRepository
import com.example.gymbuddy.data.Scheda
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class SchedeViewModel(private val gymRepository: GymRepository) : ViewModel() {

    private val _schedeUiState: MutableStateFlow<SchedeUiState> = MutableStateFlow(SchedeUiState())
    val schedeUiState: StateFlow<SchedeUiState> = _schedeUiState.asStateFlow()

    val schede = gymRepository.getAllSchedeStream()





    var isLoading by mutableStateOf(true)
        private set
    //CREATE AN INIT BLOCK TO GET ALL SCHEDE ASYNCHRONOUSLY

    init {
        viewModelScope.launch {
            schede.collect() { s ->
                schedeList = s
                _schedeUiState.value = _schedeUiState.value.copy(schedeList = s)
                _schedeUiState.value = _schedeUiState.value.copy(isLoading = false)
            }

        }
    }


    fun getAllSchede(): Flow<List<Scheda>> {
        val schede = gymRepository.getAllSchedeStream()
        isLoading = false
        return schede
    }

    fun getSchedaDetail(id: Int): Flow<Scheda> {
        val scheda = gymRepository.getSchedaDetail(id)
        return scheda
    }


    fun getEserciziScheda(id: Int): Flow<List<Esercizi>> {
        val esercizi = gymRepository.getEserciziScheda(id)
        return esercizi
    }

    var schedeList by mutableStateOf<List<Scheda>>(emptyList())
        private set

    var schedaDetail by mutableStateOf<SchedaDetail?>(null)
        private set

//    fun createJson(idScheda: Int) {
//        viewModelScope.launch {
//            eserciziFlow.collect() { esercizi ->
//
//
//            }
//        }
//    }


    data class SchedeUiState(
        val isLoading: Boolean = true,
        val error: String = "",
        val schedeList: List<Scheda> = emptyList(),
    )


    data class SchedaDetail(
        val id: Int,
        val nome: String,
        val descrizione: String,
        val ultimoAllenamento: String,
        val numeroEsercizi: String,
    )

    fun SchedaDetail.toScheda(): Scheda = Scheda(
        ID = id,
        titolo = nome,
        descrizione = descrizione,
        ultimoAllenamento = ultimoAllenamento,
        numeroEsercizi = numeroEsercizi
    )

    data class SchedaCompleta(
        val scheda: Scheda,
        val esercizi: List<Esercizi>
    )

    fun deleteScheda(scheda: Scheda) {
        schedeList= schedeList.filter { it.ID != scheda.ID }
        _schedeUiState.value = _schedeUiState.value.copy(schedeList = schedeList)
        viewModelScope.launch {
            gymRepository.deleteScheda(scheda)
        }

    }

    fun toggleFavorite(idScheda: Int) {
        val scheda = schedeList.find { it.ID == idScheda }
        val newScheda = scheda?.copy(isFavorite = !scheda.isFavorite)
        viewModelScope.launch {
            gymRepository.updateScheda(newScheda!!)
            update()
        }
    }

    fun update(){
        viewModelScope.launch {
            schede.collect() { s ->
                schedeList = s
                _schedeUiState.value = _schedeUiState.value.copy(schedeList = s)
                _schedeUiState.value = _schedeUiState.value.copy(isLoading = false)
            }
        }
    }
}