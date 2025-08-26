package com.example.gymbuddy

import android.app.Application
import com.example.gymbuddy.data.AppContainer
import com.example.gymbuddy.data.AppDataContainer

class GymApplication : Application(){
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}