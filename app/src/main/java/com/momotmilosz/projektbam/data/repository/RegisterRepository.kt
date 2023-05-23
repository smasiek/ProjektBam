package com.momotmilosz.projektbam.data.repository

import com.momotmilosz.projektbam.data.Result
import com.momotmilosz.projektbam.data.database.User
import com.momotmilosz.projektbam.data.datasource.RegisterDataSource
import com.momotmilosz.projektbam.data.model.RegisteredUser

class RegisterRepository(private val registerDataSource: RegisterDataSource) {

    /* suspend fun register(username: String, password: String): RegisteredUser {
            return registerDataSource.register(username, password)
        }*/

    fun register(username: String, password: String): Result<RegisteredUser> {
        return Result.Success(registerDataSource.register(username, password))
    }

    /*suspend fun register(username: String, password: String): Result<RegisteredUser> {
    val user = registerDataSource.getUser(username)
    try {
        if (user.userName.isEmpty()) {
            val registeredUser = registerDataSource.register(username, password)
            return Result.Success(registeredUser)
        } else {
            throw UserExistsException()
        }
    } catch (e: Throwable) {
        return Result.Error(IOException("Error logging in", e))
    }
}*/

    fun getUserByUsername(userName: String): User? {
        return registerDataSource.getUser(userName)
    }
}