package com.momotmilosz.projektbam.data

import com.momotmilosz.projektbam.Application
import com.momotmilosz.projektbam.data.database.User
import com.momotmilosz.projektbam.data.model.LoggedInUser
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


    private fun saveData(user: String, password: String) {
        GlobalScope.launch {
            (Application.appContext as Application).database.userDao().insert(
                User(userName = user, password = password)
            )
        }
    }
}