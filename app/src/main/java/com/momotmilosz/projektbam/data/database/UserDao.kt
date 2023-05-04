package com.momotmilosz.projektbam.data.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.momotmilosz.projektbam.data.database.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>
    @Query("SELECT * FROM user")
    fun getAllCursor(): Cursor
    @Insert
    fun insert(user: User)
}

