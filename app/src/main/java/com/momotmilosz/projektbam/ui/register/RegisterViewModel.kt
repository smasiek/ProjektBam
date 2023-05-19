package com.momotmilosz.projektbam.ui.register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import com.momotmilosz.projektbam.Application
import com.momotmilosz.projektbam.data.Result

import com.momotmilosz.projektbam.R
import com.momotmilosz.projektbam.data.RegisterDataSource

class RegisterViewModel(private val registerDataSource: RegisterDataSource) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult


    fun register(username: String, password: String, passwordConfirm: String) {
        // can be launched in a separate asynchronous job
        val result = registerDataSource.register(username, password)

        if (result is Result.Success) {
            _registerResult.value =
                RegisterResult(success = R.string.register_success)
        } else {
            _registerResult.value = RegisterResult(error = R.string.register_failed)
        }
    }

    fun registerDataChanged(username: String, password: String, passwordConfirm: String) {
        if (!isUserNameValid(username)) {
            _registerForm.value = RegisterFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password)
        } else if (!isConfirmPasswordValid(password, passwordConfirm)) {
            _registerForm.value = RegisterFormState(passwordConfirmError = R.string.invalid_confirm_password)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        //TODO CHECK W BAZIE DANYCH CZY MAIL NIE JEST JUZ UZYWANY
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isConfirmPasswordValid(password: String, passwordConfirm: String): Boolean {
        return password == passwordConfirm
    }
}