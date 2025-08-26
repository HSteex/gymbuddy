package com.example.gymbuddy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Scheda::class, Esercizi::class, EserciziEnum::class, User::class], version = 6, exportSchema = false)
abstract class GymDatabase : RoomDatabase(){
    abstract fun schedaDao(): SchedaDao
    companion object{
        @Volatile
        private var Instance: GymDatabase? = null
        fun getDatabase(context: Context): GymDatabase{
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, GymDatabase::class.java, "gym_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }

            }
        }
    }
//    abstract fun eserciziDao(): EserciziDao
//    abstract fun eserciziEnumDao(): EserciziEnumDao

}