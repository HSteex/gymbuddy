package com.example.gymbuddy.viewModel

import android.util.Base64
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymbuddy.data.Esercizi
import com.example.gymbuddy.data.GymRepository
import com.example.gymbuddy.data.Scheda
import com.example.gymbuddy.data.SchedaExport
import com.example.gymbuddy.gzip
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets.UTF_8
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class SchedaDetailViewModel(private val gymRepository: GymRepository, savedStateHandle: SavedStateHandle) : ViewModel() {

    private val idScheda = savedStateHandle["schedaId"] ?: 0
    private val _schedaDetailUiState : MutableStateFlow<SchedaDetailUiState> = MutableStateFlow(SchedaDetailUiState())
    val schedaDetailUiState : StateFlow<SchedaDetailUiState> = _schedaDetailUiState.asStateFlow()

    private val schedaFlow = gymRepository.getSchedaDetail(idScheda)
    private val eserciziFlow = gymRepository.getEserciziScheda(idScheda)
    private val eserciziEnumFlow = gymRepository.getEserciziEnum()
    private lateinit var scheda: Scheda
    private lateinit var esercizi: List<Esercizi>

    private val _modifiedSchedaDetailUiState : MutableStateFlow<SchedaDetailUiState> = MutableStateFlow(SchedaDetailUiState())
    val modifiedSchedaDetailUiState : StateFlow<SchedaDetailUiState> = _modifiedSchedaDetailUiState.asStateFlow()

    var editMode by mutableStateOf(false)
        private set

    var eserciziEnum by mutableStateOf(emptyList<String>())
        private set

    init {
        if(idScheda!=0) {
            viewModelScope.launch {
                schedaFlow.collect() { s ->
                    scheda = s
                    _schedaDetailUiState.value = _schedaDetailUiState.value.copy(scheda = scheda)
                    eserciziFlow.collect() { e ->
                        esercizi = e
                        _schedaDetailUiState.value =
                            _schedaDetailUiState.value.copy(esercizi = esercizi)
                        _schedaDetailUiState.value =
                            _schedaDetailUiState.value.copy(isLoading = false)
                    }
                }
            }

        }else{
            _schedaDetailUiState.value = _schedaDetailUiState.value.copy(isLoading = false)
            editMode=true
            _modifiedSchedaDetailUiState.value = _modifiedSchedaDetailUiState.value.copy(isLoading = false)
        }
        viewModelScope.launch {
            eserciziEnumFlow.collect() { e ->
                eserciziEnum = e.filter { it != "PAUSA" }

            }
        }


    }

    fun schedaToJson(): String{
        val gson = Gson()
        val schedaExport: SchedaExport = SchedaExport(scheda, esercizi)
        val json = gson.toJson(schedaExport)
        //jsonToScheda(json)
        return Base64.encodeToString(gzip(json), Base64.DEFAULT)

    }


    fun enterEditMode(){
        _modifiedSchedaDetailUiState.value=_schedaDetailUiState.value
        editMode=true
    }

    fun exitEditMode(): Boolean{
        if (idScheda==0){
            return false
        }
        editMode=false
        return true
    }





    fun updateTitle(title: String){
        _modifiedSchedaDetailUiState.value=_modifiedSchedaDetailUiState.value.copy(scheda = _modifiedSchedaDetailUiState.value.scheda.copy(titolo=title))
    }

    fun updateDesc(desc: String){
        _modifiedSchedaDetailUiState.value=_modifiedSchedaDetailUiState.value.copy(scheda = _modifiedSchedaDetailUiState.value.scheda.copy(descrizione=desc))
    }

    fun deleteEsercizio(esercizio: Esercizi){
        _modifiedSchedaDetailUiState.value=_modifiedSchedaDetailUiState.value.copy(esercizi = _modifiedSchedaDetailUiState.value.esercizi.filter { it.ID != esercizio.ID })
    }

    fun addPausa(){
        val ordine= _modifiedSchedaDetailUiState.value.esercizi.maxOfOrNull { it.ordine } ?: 0
        _modifiedSchedaDetailUiState.value=_modifiedSchedaDetailUiState.value.copy(esercizi = _modifiedSchedaDetailUiState.value.esercizi.plus(Esercizi((_modifiedSchedaDetailUiState.value.esercizi.maxOfOrNull { it.ID } ?: 0)+1,idScheda,false,"PAUSA",0,true,60,ordine+1)))
    }

    fun addEsercizio(){
        val ordine= _modifiedSchedaDetailUiState.value.esercizi.maxOfOrNull { it.ordine } ?: 0
        _modifiedSchedaDetailUiState.value=_modifiedSchedaDetailUiState.value.copy(esercizi = _modifiedSchedaDetailUiState.value.esercizi.plus(Esercizi(ID = (_modifiedSchedaDetailUiState.value.esercizi.maxOfOrNull { it.ID } ?: 0)+1,idScheda,true,eserciziEnum.first(),0,false,0,ordine+1)))
    }

    fun changeDurataPausa(durata: String, esercizio: Esercizi){
        val _durata = durata.toIntOrNull() ?: 0
        _modifiedSchedaDetailUiState.value=_modifiedSchedaDetailUiState.value.copy(esercizi = _modifiedSchedaDetailUiState.value.esercizi.map { if(it.ID==esercizio.ID) it.copy(durata=_durata) else it })
    }

    fun changeNomeEsercizio(nome: String, esercizio: Esercizi){
        _modifiedSchedaDetailUiState.value=_modifiedSchedaDetailUiState.value.copy(esercizi = _modifiedSchedaDetailUiState.value.esercizi.map { if(it.ID==esercizio.ID) it.copy(nome=nome) else it })
    }

    fun changeRipetizioni(ripetizioni: String, esercizio: Esercizi){
        val _ripetizioni = ripetizioni.toIntOrNull() ?: 0
        _modifiedSchedaDetailUiState.value=_modifiedSchedaDetailUiState.value.copy(esercizi = _modifiedSchedaDetailUiState.value.esercizi.map { if(it.ID==esercizio.ID) it.copy(ripetizioni=_ripetizioni) else it })
    }


    fun saveScheda(){
        if(idScheda!=0) {
            //Reorder esercizi.ordine
            var ordine = 1
            var newEserciziList: List<Esercizi> = emptyList()
            for (esercizio in _modifiedSchedaDetailUiState.value.esercizi) {
                newEserciziList = newEserciziList.plus(esercizio.copy(ordine = ordine, ID = 0))
                ordine++
            }
            val numeroEsercizi = newEserciziList.filter { it.nome != "PAUSA" }.size
            _modifiedSchedaDetailUiState.value = _modifiedSchedaDetailUiState.value.copy(
                esercizi = newEserciziList,
                scheda = _modifiedSchedaDetailUiState.value.scheda.copy(numeroEsercizi = numeroEsercizi.toString())
            )
            //Save scheda
            viewModelScope.launch {
                gymRepository.updateScheda(_modifiedSchedaDetailUiState.value.scheda)
                gymRepository.deleteEserciziScheda(idScheda)
                gymRepository.insertEsercizi(_modifiedSchedaDetailUiState.value.esercizi)
                _schedaDetailUiState.value = _modifiedSchedaDetailUiState.value
                editMode = false
            }
        }else{
            viewModelScope.launch {
                val numeroEsercizi = _modifiedSchedaDetailUiState.value.esercizi.filter { it.nome != "PAUSA" }.size
                _modifiedSchedaDetailUiState.value = _modifiedSchedaDetailUiState.value.copy(
                    scheda = _modifiedSchedaDetailUiState.value.scheda.copy(numeroEsercizi = numeroEsercizi.toString(), ultimoAllenamento = "Mai")
                )
                val idScheda= gymRepository.insertScheda(_modifiedSchedaDetailUiState.value.scheda)
                var ordine = 1
                var newEserciziList: List<Esercizi> = emptyList()
                for (esercizio in _modifiedSchedaDetailUiState.value.esercizi) {
                    newEserciziList = newEserciziList.plus(esercizio.copy(ordine = ordine, ID = 0, ID_scheda = idScheda.toInt()))
                    ordine++
                }

                _modifiedSchedaDetailUiState.value = _modifiedSchedaDetailUiState.value.copy(
                    esercizi = newEserciziList,
                    scheda = _modifiedSchedaDetailUiState.value.scheda.copy(numeroEsercizi = numeroEsercizi.toString())
                )
                gymRepository.insertEsercizi(_modifiedSchedaDetailUiState.value.esercizi)
                _schedaDetailUiState.value = _modifiedSchedaDetailUiState.value
                editMode = false
            }
        }
    }








    data class SchedaDetailUiState(
        val scheda: Scheda = Scheda(0,"","","",""),
        val esercizi: List<Esercizi> = emptyList(),
        val isLoading: Boolean = true,
        )



}