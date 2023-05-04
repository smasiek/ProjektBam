package com.momotmilosz.projektbam.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.momotmilosz.projektbam.data.database.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}