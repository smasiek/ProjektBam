package com.momotmilosz.projektbam.data.repository

import com.momotmilosz.projektbam.SecretApplication
import com.momotmilosz.projektbam.data.Result
import com.momotmilosz.projektbam.data.database.User
import com.momotmilosz.projektbam.data.datasource.RegisterDataSource
import com.momotmilosz.projektbam.data.model.RegisteredUser
import com.momotmilosz.projektbam.data.security.SecretManager

class RegisterRepository(private val registerDataSource: RegisterDataSource) {

    fun register(username: String, password: String): Result<RegisteredUser> {
        val secretManager = SecretManager()
        val context = SecretApplication.appContext as SecretApplication
        secretManager.createNewKeys(username, context)
        val encryptedPassword = secretManager.encryptString(username,password,context)
        return Result.Success(registerDataSource.register(username, encryptedPassword))
    }

    fun getUserByUsername(userName: String): User? {
        return registerDataSource.getUser(userName)
    }
}