package com.sulton.belibijak.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import com.sulton.belibijak.data.local.DataRekomend
import com.sulton.belibijak.data.remote.RecommendationsItem
import com.sulton.belibijak.data.repository.BeBiRepository
import com.sulton.belibijak.data.repository.Injection
import kotlinx.coroutines.launch
import java.io.StringReader

class RecommendViewModel(private val beBiRepository: BeBiRepository) : ViewModel(){

    val loading = MutableLiveData<Boolean>()

    val recommendItem = MutableLiveData<List<DataRekomend>>()
    val recomndedList = MutableLiveData<List<RecommendationsItem?>>()

    val data = listOf(
            DataRekomend(
                "Monthly Bundle",
                "https://drive.google.com/uc?export=view&id=1QwWgJY77cwAjNHt9EhVQyRUOLS8ZTPVg"
            ),
            DataRekomend(
                "Perishable Bundle",
                "https://drive.google.com/uc?export=view&id=1o2SrLudr9CrsuXwo0317xKveEql11e9c"
            ),
            DataRekomend(
                "Vegetables Bundle",
                "https://drive.google.com/uc?export=view&id=1XhSlfysKlSmKGWWfmK0_u-y9b0gRjI4o"
            )
        )

    fun getRecommendItem(double: Double){
        viewModelScope.launch {

            loading.postValue(true)
            val responseItem = beBiRepository.getRecommendation(double)
            if (responseItem.isSuccessful){
                val recomend = responseItem.body()?.recommendations
                recomndedList.postValue(recomend!!)
                val data = data
                data[0].recomended = responseItem.body()
                recommendItem.postValue(data)
                Log.d("RecommendedListData", responseItem.body()?.recommendations!!.toString())
                loading.postValue(false)

            }else {
                loading.postValue(false)

            }

        }
    }
}

class StoryModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecommendViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecommendViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}