package com.momotmilosz.projektbam

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.momotmilosz.projektbam.data.database.AppDatabase
import com.momotmilosz.projektbam.data.database.User

class Application : Application() {
    val database by lazy {
        Room.databaseBuilder(appContext, AppDatabase::class.java, "secretDatabase")
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {

        lateinit var appContext: Context

    }
}