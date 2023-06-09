package com.sulton.belibijak.data.repository

import com.sulton.belibijak.data.local.UserPreference
import com.sulton.belibijak.data.remote.ApiService
import com.sulton.belibijak.data.remote.BudgetPost
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class BeBiRepository (private val apiService: ApiService,
                      private val userPreference: UserPreference
)
{
    private var authKey: String =
        runBlocking {
            userPreference.getToken().first()
        }
    suspend fun getRecommendation(double: Double) = apiService.getRecommendation(BudgetPost(budget = double))


}