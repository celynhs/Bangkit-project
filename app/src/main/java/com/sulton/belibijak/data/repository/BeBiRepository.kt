package com.sulton.belibijak.data.repository

import com.sulton.belibijak.data.local.UserPreference
import com.sulton.belibijak.data.remote.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class BeBiRepository (private val apiService: ApiService,
                      private val userPreference: UserPreference )
{
    private var authKey: String =
        runBlocking {
            userPreference.getToken().first()
        }

}