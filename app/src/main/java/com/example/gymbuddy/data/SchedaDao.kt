package com.example.gymbuddy.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SchedaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertScheda(scheda: Scheda): Long
    @Update
    suspend fun updateScheda(scheda: Scheda)
    @Delete
    suspend fun deleteScheda(scheda: Scheda)
    @Query("SELECT * FROM Scheda ORDER BY isFavorite DESC")
    fun getAllSchede(): Flow<List<Scheda>>
    @Query("SELECT * FROM Scheda WHERE ID = :id")
    fun getSchedaDetail(id: Int): Flow<Scheda>
    @Query("SELECT * FROM Esercizi WHERE ID_scheda = :id ORDER BY ordine ASC")
    fun getEserciziScheda(id: Int): Flow<List<Esercizi>>

    @Query("UPDATE Scheda SET ultimoAllenamento = :data WHERE ID = :id")
    suspend fun updateUltimoAllenamento(id: Int, data: String)

    @Query("SELECT nome FROM EserciziEnum ORDER BY nome ASC")
    fun getEserciziEnum(): Flow<List<String>>

    @Query("DELETE FROM Esercizi WHERE ID_scheda = :id")
    suspend fun deleteEserciziScheda(id: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEsercizi(esercizi: List<Esercizi>)

    @Query("SELECT * FROM User WHERE username = :username AND password = :password")
    fun getUser(username: String, password: String): Flow<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Query("INSERT INTO EserciziEnum (Nome) VALUES ('PAUSA'), ('Panca piana'), ('Squat'), ('Stacchi da terra'), ('Leg press'), ('Pull-up'), ('Push-up'), ('Shoulder press'), ('Curl con bilanciere'), ('Affondi'), ('Burpees'), ('Plank'), ('Mountain climbers'), ('Leg curl'), ('Hip thrust'), ('Deadlift'), ('Chest fly'), ('Side lateral raise'), ('Tricep dip'), ('Bicep curl con manubri'), ('Russian twist'), ('Trazioni alla sbarra'), ('Leg extension'), ('Wall sit'), ('Sit-up'), ('Romanian deadlift'), ('Push press'), ('Curl con manubri'), ('Plank lateral'), ('Leg raise');")
    suspend fun insertEserciziEnum()

}