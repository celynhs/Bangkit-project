package com.sulton.belibijak.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.sulton.belibijak.MainActivity
import com.sulton.belibijak.data.local.UserPreference
import com.sulton.belibijak.databinding.ActivitySplashBinding
import com.sulton.belibijak.ui.auth.AuthActivity
import com.sulton.belibijak.ui.auth.LoginActivity
import com.sulton.belibijak.ui.auth.LoginViewModel
import com.sulton.belibijak.ui.auth.ViewModelFactory

class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val pref = UserPreference(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]

        viewModel.getLoginStatus().observe(this) { status: Boolean ->
            if (status) {
                finish()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                finish()
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
            }

        }

    }


}