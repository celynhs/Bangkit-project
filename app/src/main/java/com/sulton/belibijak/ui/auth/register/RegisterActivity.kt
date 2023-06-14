package com.sulton.belibijak.ui.auth.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sulton.belibijak.R
import com.sulton.belibijak.databinding.ActivityRegisterBinding
import com.sulton.belibijak.databinding.RegisterAction2Binding
import com.sulton.belibijak.databinding.RegisterAction3Binding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var binding2: RegisterAction2Binding
    private lateinit var binding3: RegisterAction3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnBack.setOnClickListener {
                super.onBackPressed()
            }
            nextButton.setOnClickListener {
                if (validateForm()){
                    binding2 = RegisterAction2Binding.inflate(layoutInflater)
                    setContentView(binding2.root)


                    binding2.btnBack2Reg.setOnClickListener {

                        setContentView(binding.root)
                    }
                    binding2.nextButton2.setOnClickListener {
                        binding3 = RegisterAction3Binding.inflate(layoutInflater)
                        setContentView(binding3.root)

                }

                }
            }
        }
    }
    fun validateForm(): Boolean{
       with(binding){
           val nama = edRegisterName.text.toString().trim()
           val email = edRegisterEmail.text.toString().trim()
           val pw = edRegisterPassword.text.toString().trim()

           if (nama.isEmpty()){
               edRegisterName.error = getString(R.string.requir)
               return false
           }
           if (email.isEmpty()){
               edRegisterEmail.error = getString(R.string.requir)
               return false
           }
           if (pw.isEmpty()){
               edRegisterPassword.error = getString(R.string.requir)
               return false
       }
           return true
       }
    }
}