package com.momotmilosz.projektbam.data.datasource

import com.momotmilosz.projektbam.data.database.User
import com.momotmilosz.projektbam.data.database.UserDao
import com.momotmilosz.projektbam.data.model.RegisteredUser

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class RegisterDataSource(private val userDao: UserDao) {

    fun register(username: String, password: String): RegisteredUser {
        userDao.insert(User(userName = username, password = password, uid = null))
        return RegisteredUser(username)
    }

    fun getUsers(): List<User> {
        return userDao.getAll()
    }

    fun getUser(username: String): User? {
        return userDao.getUser(username)
    }
}