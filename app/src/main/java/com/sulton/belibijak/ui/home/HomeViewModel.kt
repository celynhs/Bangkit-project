package com.sulton.belibijak.ui.home

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sulton.belibijak.data.remote.ApiConfig
import com.sulton.belibijak.data.remote.ItemsItem
import com.sulton.belibijak.data.remote.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<List<ItemsItem>?>()
    val listUsers: MutableLiveData<List<ItemsItem>?> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _eResponse = MutableLiveData<String>()
    val eResponse: LiveData<String> = _eResponse

    fun searchUser(query : String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d("ListUser", response.body()?.items.toString())
                    _listUsers.value = response.body()?.items
                } else {
                    _eResponse.value = "Request Failed"
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _eResponse.value = "Request Failed"
            }
        })
    }

}