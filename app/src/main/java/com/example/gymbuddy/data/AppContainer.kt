package com.example.gymbuddy.data

import android.content.Context

interface AppContainer{
    val gymRepository: GymRepository
}
class AppDataContainer (private val context: Context) : AppContainer{
    override val gymRepository: GymRepository by lazy {
        OfflineGymRepository(GymDatabase.getDatabase(context).schedaDao())
    }
}