package com.sulton.belibijak.ui.auth

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.sulton.belibijak.data.local.UserPreference
import com.sulton.belibijak.data.remote.ApiConfig
import com.sulton.belibijak.data.remote.AuthResponse
import com.sulton.belibijak.data.remote.SignIn
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private  val pref : UserPreference) : ViewModel() {

    private val _loginResult = MutableLiveData<String>()
    val loginResult: LiveData<String> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun getLoginStatus(): LiveData<Boolean> = pref.getLoginStatus().asLiveData()

    fun logOut() {
        viewModelScope.launch {
            pref.deleteAllPreferences()
        }
    }

    fun getToken(): LiveData<String> = pref.getToken().asLiveData()

    fun getBudget(): LiveData<Double> = pref.getBudget().asLiveData()

    fun postLogin(signIn: SignIn) {
        _isLoading.value = true
        val client = ApiConfig.getLoginService().signIn(signIn)
        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {

                if (response.isSuccessful) {
                    Log.d("ListUser", response.body().toString())
                    val responseItem = response.body()
                    _loginResult.value = "Success"
                    viewModelScope.launch {
                        pref.setLoginStatus(
                            true,
                            response.body()?.name.toString(),
                            response.body()?.email.toString(),
                            response.body()?.token.toString(),
                            responseItem?.address.toString(),
                            responseItem?.budget!!.toDouble()
                        )
                                   }

                } else {
                    _loginResult.value = "Gagal"
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _loginResult.value = "Gagal"
                _isLoading.value = false
                Log.e(ContentValues.TAG, "Fail: ${t.message.toString()}")
            }
        })
    }

}

class ViewModelFactory(private val pref: UserPreference) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(pref) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}