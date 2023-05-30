package com.momotmilosz.projektbam.ui.login

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.momotmilosz.projektbam.R
import com.momotmilosz.projektbam.data.security.SecretManager
import com.momotmilosz.projektbam.databinding.ActivityLoginBinding
import com.momotmilosz.projektbam.ui.notes.NotesActivity
import com.momotmilosz.projektbam.ui.register.RegisterActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginPreferences: SharedPreferences
    private lateinit var loginPrefsEditor: SharedPreferences.Editor
    private val secretManager = SecretManager()
    private var saveLogin: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goToRegister?.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            //intent.putExtra(HelloActivity.USER_NAME_EXTRA, userName)
            startActivity(intent)
        }

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading
        val saveLoginCheck = binding.saveLoginCheckBox

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        loginPrefsEditor = loginPreferences.edit()

        saveLogin = loginPreferences.getBoolean("saveLogin", false)
        if (saveLogin) {
            restoreUser(username, password, saveLoginCheck, login)
        }


        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
                rememberUser(username.text.toString(), password.text.toString())

                val notesIntent = Intent(applicationContext, NotesActivity::class.java)
                notesIntent.putExtra("userId", loginResult.success.userId)
                startActivity(notesIntent)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            //finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        if (login.isEnabled) {
                            loginViewModel.login(
                                username.text.toString(),
                                password.text.toString()
                            )
                        } else {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.login_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }

    }

    private fun restoreUser(
        username: EditText,
        password: EditText,
        saveLoginCheck: CheckBox?,
        login: Button
    ) {
        val restoredUsername = loginPreferences.getString("username", "")
        val restoredPassword = loginPreferences.getString("password", "")

        val decryptedPassword =
            secretManager.decryptString(restoredUsername, restoredPassword, this)

        username.setText(restoredUsername)
        password.setText(decryptedPassword)
        saveLoginCheck?.isChecked = true
        login.isEnabled = true
    }

    private fun rememberUser(username: String, password: String) {
        if (binding.saveLoginCheckBox?.isChecked == true) {
            loginPrefsEditor.putBoolean("saveLogin", true)
            loginPrefsEditor.putString("username", username)

            val encryptedPassword = secretManager.encryptString(username, password, this)
            loginPrefsEditor.putString("password", encryptedPassword)
            loginPrefsEditor.commit()
        } else {
            loginPrefsEditor.clear()
            loginPrefsEditor.commit()
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}