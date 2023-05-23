package com.momotmilosz.projektbam.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.momotmilosz.projektbam.SecretApplication
import com.momotmilosz.projektbam.data.datasource.RegisterDataSource
import com.momotmilosz.projektbam.data.repository.RegisterRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class RegisterViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(
                registerRepository = RegisterRepository(
                    registerDataSource = RegisterDataSource((SecretApplication.appContext as SecretApplication).database.userDao())
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}