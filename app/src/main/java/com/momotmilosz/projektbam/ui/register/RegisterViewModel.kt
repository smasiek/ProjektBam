package com.momotmilosz.projektbam.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momotmilosz.projektbam.R
import com.momotmilosz.projektbam.data.Result
import com.momotmilosz.projektbam.data.repository.RegisterRepository
import kotlinx.coroutines.*

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun register(username: String, password: String, passwordConfirm: String) {
        // can be launched in a separate asynchronous job
        if (username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            _registerResult.postValue(RegisterResult(error = R.string.register_failed_no_values))
        } else {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    val userByUsername = registerRepository.getUserByUsername(userName = username)
                    if (userByUsername != null) {
                        _registerResult.postValue(RegisterResult(error = R.string.register_failed_username_used))
                    } else {
                        register(username, password)
                    }
                }
            }
        }
    }

    private fun register(username: String, password: String): Job = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val result = registerRepository.register(username, password)
            if (result is Result.Success) {
                _registerResult.postValue(RegisterResult(success = R.string.register_success))
            } else {
                _registerResult.postValue(RegisterResult(error = R.string.register_failed))
            }
        }
    }

    fun registerDataChanged(username: String, password: String, passwordConfirm: String) {
        if (!isUserNameValid(username)) {
            _registerForm.postValue(RegisterFormState(usernameError = R.string.invalid_username))
        } else if (!isPasswordValid(password)) {
            _registerForm.postValue(RegisterFormState(passwordError = R.string.invalid_password))
        } else if (!isConfirmPasswordValid(password, passwordConfirm)) {
            _registerForm.postValue(
                RegisterFormState(passwordConfirmError = R.string.invalid_confirm_password)
            )
        } else {
            _registerForm.postValue(RegisterFormState(isDataValid = true))
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
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