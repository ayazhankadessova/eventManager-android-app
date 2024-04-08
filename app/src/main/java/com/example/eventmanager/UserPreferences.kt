package com.example.eventmanager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userInfo")
        val User_id = stringPreferencesKey("userID")
    }

    val getUserId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[User_id]
        }

    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[User_id] = userId
        }
    }

    suspend fun removeUserId() {
        context.dataStore.edit { preferences ->
            preferences.remove(User_id)
        }
    }
}