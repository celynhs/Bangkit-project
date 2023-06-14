package com.sulton.belibijak.ui.auth

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.sulton.belibijak.data.local.UserPreference
import com.sulton.belibijak.data.remote.ApiConfig
import com.sulton.belibijak.data.remote.LogResponse
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

    fun postLogin(signIn: SignIn) {
        _isLoading.value = true
        val client = ApiConfig.getLoginService().reqLogin(signIn)
        client.enqueue(object : Callback<LogResponse> {
            override fun onResponse(call: Call<LogResponse>, response: Response<LogResponse>) {

                if (response.isSuccessful) {
                    Log.d("ListUser", response.body()?.loginResult.toString())
                    _loginResult.value = "Success"
                    viewModelScope.launch {
                        pref.setLoginStatus(
                            true,
                            response.body()?.loginResult?.name.toString(),
                            response.body()?.loginResult?.userId.toString(),
                            response.body()?.loginResult?.token.toString()
                        )
                    }

                } else {
                    _loginResult.value = "Gagal"
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<LogResponse>, t: Throwable) {
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