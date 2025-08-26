package com.example.gymbuddy.viewModel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DataStoreManager (context: Context) {
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("user_preferences")
    private val dataStore = context.dataStore

    companion object{
        val darkModeKey= booleanPreferencesKey("DARK_MODE_KEY")
        val username= stringPreferencesKey("USERNAME")


    }

    suspend fun saveDarkMode(darkMode: Boolean){
        dataStore.edit { preferences ->
            preferences[darkModeKey] = darkMode
        }
    }

    suspend fun setUsername(username: String){
        dataStore.edit { preferences ->
            preferences[DataStoreManager.username] = username
        }
    }

    fun getUsername() = dataStore.data.map {
        it[username] ?: ""
    }

    fun getDarkMode() = dataStore.data.map {
        it[darkModeKey] ?: false
    }
}