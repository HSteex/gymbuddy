package com.example.gymbuddy.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymbuddy.data.GymRepository
import com.example.gymbuddy.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import java.util.prefs.Preferences

class LoginViewModel(private val gymRepository: GymRepository): ViewModel(){
    private val _loginUiState : MutableStateFlow<LoginUiState> = MutableStateFlow(
        LoginUiState()
    )
    val loginUiState : StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private val _registerUiState : MutableStateFlow<RegisterUiState> = MutableStateFlow(
        RegisterUiState()
    )
    val registerUiState : StateFlow<RegisterUiState> = _registerUiState.asStateFlow()

    var login by mutableStateOf(false)

    fun login(username: String, password: String){
        viewModelScope.launch {
            gymRepository.getUser(username, password).collect() { u ->
                if (u != null) {
                    _loginUiState.value = _loginUiState.value.copy(
                        trovato = true,
                    )
                    login=true
                } else {
                    _loginUiState.value = _loginUiState.value.copy(
                        error = true,
                    )
                }
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            try {
                gymRepository.insertUser(User(username, password, "", ""))

            } catch (e: Exception) {
                _registerUiState.value = _registerUiState.value.copy(
                    error = true,
                )
            }

        }
    }

    fun switchRegisterMode(){
        _registerUiState.value = _registerUiState.value.copy(
            registerMode = !_registerUiState.value.registerMode,
        )
    }

    fun updateLoginUiState(username: String, password: String){
        _loginUiState.value = _loginUiState.value.copy(
            username = username,
            password = password,
        )
    }
    fun updateRegisterUiState(username: String, password: String, confermaPassword: String){
        _registerUiState.value = _registerUiState.value.copy(
            username = username,
            password = password,
            confermaPassword = confermaPassword
        )
    }

    data class LoginUiState(
        val username: String = "",
        val password: String = "",
        val error: Boolean = false,
        val trovato: Boolean = false
    )
    data class RegisterUiState(
        val registerMode: Boolean= false,
        val username: String = "",
        val password: String = "",
        val confermaPassword: String = "",
        val error: Boolean = false
    )

}