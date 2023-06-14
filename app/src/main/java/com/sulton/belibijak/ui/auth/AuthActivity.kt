package com.sulton.belibijak.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sulton.belibijak.databinding.ActivityAuthBinding
import com.sulton.belibijak.ui.auth.register.RegisterActivity

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.apply {
            authBtnLogin.setOnClickListener {
                startActivity(Intent(this@AuthActivity, LoginActivity::class.java))
            }
            authBtnRegister.setOnClickListener {
                startActivity(Intent(this@AuthActivity, RegisterActivity::class.java))

            }
        }
    }

}