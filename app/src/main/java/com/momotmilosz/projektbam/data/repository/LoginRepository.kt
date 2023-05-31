package com.momotmilosz.projektbam.data.repository

import android.security.keystore.UserNotAuthenticatedException
import com.momotmilosz.projektbam.SecretApplication
import com.momotmilosz.projektbam.data.Result
import com.momotmilosz.projektbam.data.datasource.LoginDataSource
import com.momotmilosz.projektbam.data.model.LoggedInUser
import com.momotmilosz.projektbam.data.security.SecretManager
import com.momotmilosz.projektbam.exceptions.UserDoesntExistsException

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    companion object {
        var user: LoggedInUser? = null
            private set
    }

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val secretManager = SecretManager()
        val context = SecretApplication.appContext as SecretApplication
        val user = dataSource.getUser(username)

        val decryptedPassword: String
        if (user != null) {
            decryptedPassword = secretManager.decryptString(username, user.password, context)
            if (decryptedPassword == password) {
                val loggedInUser = LoggedInUser(user.uid?:throw UserNotAuthenticatedException("No uid in cache"), user.userName)
                setLoggedInUser(loggedInUser)
                return Result.Success(loggedInUser)
            }
        }

        return Result.Error(UserDoesntExistsException())
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}