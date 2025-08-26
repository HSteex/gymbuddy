package com.example.gymbuddy.data

import kotlinx.coroutines.flow.Flow

class OfflineGymRepository(private val schedaDao: SchedaDao) : GymRepository {
    override fun getAllSchedeStream(): Flow<List<Scheda>> = schedaDao.getAllSchede()

    override fun getSchedaDetail(id: Int): Flow<Scheda> = schedaDao.getSchedaDetail(id)

    override fun getEserciziScheda(id: Int): Flow<List<Esercizi>> = schedaDao.getEserciziScheda(id)

    override suspend fun insertScheda(scheda: Scheda) = schedaDao.insertScheda(scheda)

    override suspend fun deleteScheda(scheda: Scheda) = schedaDao.deleteScheda(scheda)

    override suspend fun updateScheda(scheda: Scheda) = schedaDao.updateScheda(scheda)

    override suspend fun updateUltimoAllenamento(id: Int, data: String) = schedaDao.updateUltimoAllenamento(id, data)

    override fun getEserciziEnum(): Flow<List<String>> = schedaDao.getEserciziEnum()

    override suspend fun deleteEserciziScheda(id: Int) = schedaDao.deleteEserciziScheda(id)

    override suspend fun insertEsercizi(esercizi: List<Esercizi>) = schedaDao.insertEsercizi(esercizi)

    override fun getUser(username: String, password: String): Flow<User> = schedaDao.getUser(username, password)

    override suspend fun insertUser(user: User): Long = schedaDao.insertUser(user)

    override suspend fun insertEserciziEnum() = schedaDao.insertEserciziEnum()



}