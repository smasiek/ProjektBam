package com.momotmilosz.projektbam.data.database

import android.database.Cursor
import androidx.room.*


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM User WHERE user_name=:username and password=:password")
    fun loginCheck(username: String, password: String): User

    @Query("SELECT * FROM User WHERE user_name=:username")
    fun getUser(username: String): User

//    fun loginCheck(username: String, password: String): LiveData<User>

/*    @Transaction
    fun loadFromUser(username: String, password: String): LiveData<User> {
        return loginCheck(username, password)
    }*/

    @Query("SELECT * FROM user")
    fun getAllCursor(): Cursor

    @Insert
    fun insert(user: User)

    @Insert
    fun insertAll(users: User)

    @Delete
    fun delete(user: User)
}

