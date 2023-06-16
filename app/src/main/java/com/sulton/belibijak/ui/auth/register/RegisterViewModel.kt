package com.sulton.belibijak.ui.auth.register

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sulton.belibijak.data.remote.ApiConfig
import com.sulton.belibijak.data.remote.ResponseReg
import com.sulton.belibijak.data.remote.SignUp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _regResult = MutableLiveData<String>()
    val regResult: LiveData<String> = _regResult

    fun reqRegister(signUp: SignUp) {
        _isLoading.value = true
        val client = ApiConfig.getLoginService().signUp(signUp)
        client.enqueue(object : Callback<ResponseReg> {
            override fun onResponse(call: Call<ResponseReg>, response: Response<ResponseReg>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d("regOnSuccess", response.body().toString())
                    _regResult.value = "Berhasil Register"
                } else {
                    _regResult.value = response.body().toString()
                    Log.e(ContentValues.TAG, "regOnFailure: ${response.message()}")
                }

            }

            override fun onFailure(call: Call<ResponseReg>, t: Throwable) {
             //  Log.e(ContentValues.TAG, "regOnFailure: ${t.message.toString()}")
                _isLoading.value = false
                _regResult.value = t.message.toString()
            }
        })
    }

}