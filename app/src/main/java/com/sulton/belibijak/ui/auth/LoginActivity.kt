package com.sulton.belibijak.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sulton.belibijak.MainActivity
import com.sulton.belibijak.R
import com.sulton.belibijak.data.local.UserPreference
import com.sulton.belibijak.data.remote.SignIn
import com.sulton.belibijak.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val pref = UserPreference.getInstance(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]
        binding.login.isClickable = false


        binding.btnBack.setOnClickListener {
            super.onBackPressed()
        }
        binding.login.setOnClickListener {
            loginUser()
        }
        viewModel.isLoading.observe(this){
            showLoading(it)

        }
        viewModel.loginResult.observe(this) { message ->
            Toast.makeText(this, "Login $message", Toast.LENGTH_SHORT).show()
            if (message.equals("Success")) {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun loginUser() {
    val signIn = SignIn (
        binding.edLoginEmail.text.toString(),
        binding.edLoginPassword.text.toString()
    )
        viewModel.postLogin(signIn)

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun validate(email: String, password: String): Boolean {
        return if (email.contains('@') && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            password.length >= 8
        } else {
            false
        }

    }

}