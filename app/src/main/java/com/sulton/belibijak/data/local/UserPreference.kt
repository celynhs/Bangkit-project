package com.sulton.belibijak.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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

        val address = intPreferencesKey("address")

    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[UserPref.token] ?: ""
        }
    }

    fun getLoginStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[UserPref.LOGIN_KEY] ?: false
        }
    }

    suspend fun setLoginStatus(isLogged: Boolean, name: String, userId: String, token: String) {
        dataStore.edit { preferences ->
            preferences[UserPref.LOGIN_KEY] = isLogged
            preferences[UserPref.name] = name
            preferences[UserPref.email] = userId
            preferences[UserPref.token] = token
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