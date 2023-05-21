package com.momotmilosz.projektbam.data.datasource

import android.util.Log
import com.momotmilosz.projektbam.SecretApplication
import com.momotmilosz.projektbam.data.Result
import com.momotmilosz.projektbam.data.database.User
import com.momotmilosz.projektbam.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            // TODO: handle loggedInUser authentication
            Log.d("check","check")
            val user = checkUserInDatabase(username, password)
            //val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            Result.Success(LoggedInUser(user.uid.toString(), user.userName))
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    private fun checkUserInDatabase(username: String, password: String): User {

        // TODO: trzeba dodać ViewModel i Repo do logowania pewnie, tak żeby dało się
        //  niesynchronicznie sprawdzić czy user istnieje w bazie danych i jeśli tak to zalogować.

        val users = (SecretApplication.appContext as SecretApplication).database.userDao().getAll()
        for (user in users) {
            Log.d("users",user.userName) //testowe logowanie zeby zobaczyc co sie wykonuje.
        // To sie nie wykona bo na głównym wątku odpytuje baze danych.

        }
        val user = (SecretApplication.appContext as SecretApplication).database.userDao().loginCheck(username,password)
        Log.d("user",user.userName)
        return user
    }

    fun logout() {
        // TODO: revoke authentication
    }
}