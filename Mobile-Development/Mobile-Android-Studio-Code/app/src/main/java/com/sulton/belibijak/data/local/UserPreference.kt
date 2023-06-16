package com.sulton.belibijak.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "Login"
)
class UserPreference(context: Context) {

    private val dataStore = context.dataStore

    private object UserPref {
        val LOGIN_KEY = booleanPreferencesKey("isLogged")

        val name = stringPreferencesKey("name")
        val email = stringPreferencesKey("email")
        val token = stringPreferencesKey("token")
        val address = stringPreferencesKey("address")
        val budget = doublePreferencesKey("budget")

    }
    suspend fun updateBudget(double: Double){
        dataStore.edit { pref->
            pref[UserPref.budget] = double
        }
    }
    fun getBudget(): Flow<Double> {
        return dataStore.data.map { preferences ->
            preferences[UserPref.budget] ?: 0.0
        }
    }
    fun getAddress(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[UserPref.address] ?: ""
        }
    }
    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[UserPref.token] ?: ""
        }
    }
    fun getName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[UserPref.name] ?: ""
        }
    }
    fun getLoginStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[UserPref.LOGIN_KEY] ?: false
        }
    }

    suspend fun setLoginStatus(isLogged: Boolean, name: String, userId: String, token: String, address: String, budget: Double ) {
        dataStore.edit { preferences ->
            preferences[UserPref.LOGIN_KEY] = isLogged
            preferences[UserPref.name] = name
            preferences[UserPref.email] = userId
            preferences[UserPref.token] = token
            preferences[UserPref.address] = address
            preferences[UserPref.budget] = budget
        }
    }

    suspend fun deleteAllPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(context: Context): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(context)
                INSTANCE = instance
                instance
            }
        }
    }
}