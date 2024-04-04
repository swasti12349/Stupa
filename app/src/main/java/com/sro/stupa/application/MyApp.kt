package com.sro.stupa.application

import android.app.Application
import androidx.room.Room
import com.sro.stupa.Model.AppDatabase

class MyApp : Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "my_database"
        ).build()
    }
}
