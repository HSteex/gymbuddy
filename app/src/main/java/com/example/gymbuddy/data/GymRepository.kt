package com.example.gymbuddy.data

import kotlinx.coroutines.flow.Flow

interface GymRepository {
    fun getAllSchedeStream(): Flow<List<Scheda>>

    fun getSchedaDetail(id: Int): Flow<Scheda>

    fun getEserciziScheda(id: Int): Flow<List<Esercizi>>

    suspend fun insertScheda(scheda: Scheda) : Long
    suspend fun deleteScheda(scheda: Scheda)
    suspend fun updateScheda(scheda: Scheda)

    suspend fun updateUltimoAllenamento(id: Int, data: String)

    fun getEserciziEnum(): Flow<List<String>>

    suspend fun deleteEserciziScheda(id: Int)

    suspend fun insertEsercizi(esercizi: List<Esercizi>)

    fun getUser(username: String, password: String): Flow<User>

    suspend fun insertUser(user: User): Long

    suspend fun insertEserciziEnum()







}