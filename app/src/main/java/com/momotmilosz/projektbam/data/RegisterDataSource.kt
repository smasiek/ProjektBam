package com.momotmilosz.projektbam.data

import com.momotmilosz.projektbam.SecretApplication
import com.momotmilosz.projektbam.data.database.User
import com.momotmilosz.projektbam.data.model.RegisteredUser
import com.momotmilosz.projektbam.exceptions.UserExistsException
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
        GlobalScope.launch {
            val userDao = (SecretApplication.appContext as SecretApplication).database.userDao()
            if (userDao.getUser(username).userName != "") {
                throw UserExistsException()
            }
            userDao.insert(
                User(userName = username, password = password)
            )
        }
    }
}