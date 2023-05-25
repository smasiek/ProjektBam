package com.momotmilosz.projektbam.data.datasource

import com.momotmilosz.projektbam.data.database.UserDao
import com.momotmilosz.projektbam.data.model.LoggedInUser

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(private val usersDao: UserDao) {

    fun login(username: String, password: String): LoggedInUser {
        val user = usersDao.loginCheck(username, password)
        return LoggedInUser(user.uid.toString(), user.userName)
    }

    fun logout() {
        // TODO: revoke authentication
    }
}