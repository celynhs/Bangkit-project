package com.sulton.belibijak.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.sulton.belibijak.R
import com.sulton.belibijak.data.remote.SignUp
import com.sulton.belibijak.databinding.ActivityRegisterBinding
import com.sulton.belibijak.databinding.RegisterAction2Binding
import com.sulton.belibijak.databinding.RegisterAction3Binding
import com.sulton.belibijak.ui.auth.AuthActivity
import com.sulton.belibijak.ui.auth.LoginActivity
import com.sulton.belibijak.ui.auth.LoginViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var binding2: RegisterAction2Binding
    private lateinit var binding3: RegisterAction3Binding
    private lateinit var viewModel : RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        binding2 = RegisterAction2Binding.inflate(layoutInflater)
        binding3 = RegisterAction3Binding.inflate(layoutInflater)

        setContentView(binding.root)
        nextBack()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[RegisterViewModel::class.java]

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        binding3.nextButton2.setOnClickListener {
            if (validateForm3()){
                registerUser()
            }
        }

    }

    private fun nextBack(){
        with(binding){
            btnBack.setOnClickListener {
                finish()
                val intent = Intent(this@RegisterActivity, AuthActivity::class.java)
                startActivity(intent)
            }
            nextButton.setOnClickListener {
                if (validateForm()){
                    setContentView(binding2.root)

                    binding2.btnBack2Reg.setOnClickListener {

                        setContentView(binding.root)
                    }
                    binding2.nextButton2.setOnClickListener {
                        if (validateForm2()){
                            setContentView(binding3.root)
                        }
                        binding3.btnBack2Reg.setOnClickListener {
                            setContentView(binding2.root)
                        }
                    }
                }
            }
        }
    }
    private fun validateForm(): Boolean{
       with(binding){
           val nama = edRegisterName.text.toString().trim()
           val email = edRegisterEmail.text.toString().trim()
           val pw = edRegisterPassword.text.toString().trim()

           if (nama.isEmpty()){
               edRegisterName.error = getString(R.string.requir)
               return false
           }
           if (!(email.contains('@') && Patterns.EMAIL_ADDRESS.matcher(email).matches())){
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
    private fun validateForm2(): Boolean{
        with(binding2){
            val phone = edRegisterNumbe.text.toString().trim()
            val address = edRegisterAddress.text.toString().trim()
            val city = edCityAddress.text.toString().trim()
            val zip = edZipCode.text.toString().trim()

            if (phone.isEmpty()){
                edRegisterNumbe.error = getString(R.string.requir)
                return false
            }
            if (address.isEmpty()){
                edRegisterAddress.error = getString(R.string.requir)
                return false
            }
            if (city.isEmpty()){
                edCityAddress.error = getString(R.string.requir)
            return false
             }
            if (zip.isEmpty()){
                edZipCode.error = getString(R.string.requir)
            return false
        }
            return true
        }
    }
    private fun validateForm3(): Boolean{
        with(binding3){
            val nama = edRegisterBudget.text.toString().trim()


            if (nama.isEmpty()){
                edRegisterBudget.error = getString(R.string.requir)
                return false
            }

            return true
        }
    }

    private fun registerUser(){
        val reg = SignUp(
            name = binding.edRegisterName.text.toString(),
            email = binding.edRegisterEmail.text.toString(),
            phone = binding2.edRegisterNumbe.text.toString(),
            password = binding.edRegisterPassword.text.toString(),
            address = binding2.edRegisterAddress.text.toString(),
            postCode = binding2.edZipCode.text.toString(),
            city =  binding2.edCityAddress.text.toString(),
            budget = binding3.edRegisterBudget.text.toString(),
            userType = "user"
        )
        viewModel.reqRegister(reg)
        viewModel.regResult.observe(this) { response ->
            if (response.equals("Berhasil Register")) {
                Toast.makeText(this@RegisterActivity, response.toString(), Toast.LENGTH_SHORT)
                    .show()
                finish()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding3.registerLoad.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}