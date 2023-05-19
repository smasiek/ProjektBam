package com.momotmilosz.projektbam.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.momotmilosz.projektbam.Application
import com.momotmilosz.projektbam.R
import com.momotmilosz.projektbam.data.database.User
import com.momotmilosz.projektbam.databinding.ActivityRegisterBinding
import com.momotmilosz.projektbam.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as Application).database.userDao().insert(
            User(userName = "admin@admin.pl", password = "password")
        )
        Log.d("asda","asd")

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goToRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val username = binding.usernameRegister
        val password = binding.passwordRegister
        val passwordConfirm = binding.passwordConfirmRegister
        val register = binding.register
        val loading = binding.loadingRegister

        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val registerState = it ?: return@Observer

            // disable register button unless both username / password is valid
            register.isEnabled = registerState.isDataValid

            if (registerState.usernameError != null) {
                username.error = getString(registerState.usernameError)
            }
            if (registerState.passwordError != null) {
                password.error = getString(registerState.passwordError)
            }
            if (registerState.passwordConfirmError != null) {
                passwordConfirm.error = getString(registerState.passwordConfirmError)
            }
        })

        registerViewModel.registerResult.observe(this@RegisterActivity, Observer {
            val registerResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (registerResult.error != null) {
                showLoginFailed(registerResult.error)
            }
            if (registerResult.success != null) {
                switchToLoginPage()
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy register activity once successful
            finish()
        })

        username.afterTextChanged {
            registerViewModel.registerDataChanged(
                username.text.toString(),
                password.text.toString(),
                passwordConfirm.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    username.text.toString(),
                    password.text.toString(),
                    passwordConfirm.text.toString()
                )
            }
        }

        passwordConfirm.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    username.text.toString(),
                    password.text.toString(),
                    passwordConfirm.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        registerViewModel.register(
                            username.text.toString(),
                            password.text.toString(),
                            passwordConfirm.text.toString()
                        )
                }
                false
            }

            register.setOnClickListener {
                loading.visibility = View.VISIBLE
                registerViewModel.register(
                    username.text.toString(),
                    password.text.toString(),
                    passwordConfirm.text.toString()
                )
            }
        }
    }

    private fun switchToLoginPage() {
        val success = getString(R.string.register_success)

        Toast.makeText(
            applicationContext,
            success,
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}