package com.momotmilosz.projektbam.data.datasource

import android.util.Log
import com.momotmilosz.projektbam.SecretApplication
import com.momotmilosz.projektbam.data.Result
import com.momotmilosz.projektbam.data.database.User
import com.momotmilosz.projektbam.data.database.UserDao
import com.momotmilosz.projektbam.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(private val usersDao: UserDao) {

//    val user: Flow<MutableList<User>> = usersDao.getUser()

    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            // TODO: handle loggedInUser authentication
            Log.d("check", "check")
            val user = checkUserInDatabase(username, password)
            //val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            Result.Success(LoggedInUser(user.uid.toString(), user.userName))
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    private fun checkUserInDatabase(username: String, password: String): User {
        return (SecretApplication.appContext as SecretApplication).database.userDao()
            .loginCheck(username, password)
    }

    fun logout() {
        // TODO: revoke authentication
    }
}