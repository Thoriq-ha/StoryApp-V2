package com.thor.storyapp.data.preferences.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
//import androidx.datastore.preferences.core.booleanPreferencesKey
import com.thor.storyapp.repository.auth.Login
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreSession(private val dataStore: DataStore<Preferences>) {
    private object Keys {
        val userId = stringPreferencesKey("id")
        val userName = stringPreferencesKey("name")
        val userToken = stringPreferencesKey("token")
    }


    suspend fun setUserLogin(user: Login) {
        dataStore.edit { preferences ->
            preferences[Keys.userId] = user.userId ?: ""
            preferences[Keys.userName] = user.name ?: ""
            preferences[Keys.userToken] = user.token ?: ""
        }
    }
    suspend fun deleteSession() {
        dataStore.edit { it.clear() }
    }

    val userFlow: Flow<Login> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            Login(preferences[Keys.userName],
                preferences[Keys.userId],
                preferences[Keys.userToken] )
        }
}