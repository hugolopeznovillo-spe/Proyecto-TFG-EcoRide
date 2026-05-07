package com.ecoride.app.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "ecoride_prefs")

class TokenDataStore(private val context: Context) {

    companion object {
        private val KEY_TOKEN    = stringPreferencesKey("jwt_token")
        private val KEY_ROLE     = stringPreferencesKey("user_role")
        private val KEY_USERNAME = stringPreferencesKey("username")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[KEY_TOKEN] }
    val roleFlow:  Flow<String?> = context.dataStore.data.map { it[KEY_ROLE] }
    val usernameFlow: Flow<String?> = context.dataStore.data.map { it[KEY_USERNAME] }

    suspend fun saveSession(token: String, role: String, username: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_TOKEN]    = token
            prefs[KEY_ROLE]     = role
            prefs[KEY_USERNAME] = username
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
