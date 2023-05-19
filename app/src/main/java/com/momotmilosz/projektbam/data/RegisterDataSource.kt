package com.momotmilosz.projektbam.data

import android.content.Context
import android.util.Log
import com.momotmilosz.projektbam.Application
import com.momotmilosz.projektbam.data.database.User
import com.momotmilosz.projektbam.data.model.RegisteredUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class RegisterDataSource {

    fun register(username: String, password: String): Result<RegisteredUser> {
        try {
            saveData(username, password)
            return Result.Success(RegisteredUser(username))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    private fun saveData(username: String, password: String) {
        val users =(Application.appContext as Application).database.userDao().getAll(
        )

        Log.d("New user:", "asdasd")
        Log.d("New user:", users.get(0).userName)
        (Application.appContext as Application).database.userDao().insert(
            User(userName = username, password = password)
        )
    }
}