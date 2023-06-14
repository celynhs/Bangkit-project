package com.sulton.belibijak.data.repository

import android.content.Context
import com.sulton.belibijak.data.local.UserPreference
import com.sulton.belibijak.data.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context): BeBiRepository {
        val pref = UserPreference.getInstance(context)
        val apiService = ApiConfig.getApiService()
        return BeBiRepository(apiService, pref)
    }
}