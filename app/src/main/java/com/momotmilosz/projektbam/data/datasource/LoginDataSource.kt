package com.momotmilosz.projektbam.data.datasource

import com.momotmilosz.projektbam.data.database.User
import com.momotmilosz.projektbam.data.database.UserDao

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(private val userDao: UserDao) {
    fun getUser(username: String): User? {
        return userDao.getUser(username)
    }
    fun logout() {
        // TODO: revoke authentication
    }
}