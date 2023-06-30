package com.app.storyapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "user")

class SharedPref (private val context : Context) {
    private val userId = stringPreferencesKey("userId")
    private val name = stringPreferencesKey("name")
    private val token = stringPreferencesKey("token")


    suspend fun saveToken(userIds : String,names : String, tokens : String){
        context.dataStore.edit {
            it[userId] = userIds
            it[name] = names
            it[token] = tokens
        }
    }

    val getToken : Flow<String> = context.dataStore.data
        .map {
            it[token] ?: ""
        }

    val getIdUser : Flow<String> = context.dataStore.data
        .map {
            it[userId] ?: "Undefined"
        }

    val getNama : Flow<String> = context.dataStore.data
        .map {
            it[name] ?: "Undefined"
        }

    suspend fun removeToken(){
        context.dataStore.edit {
            it.remove(userId)
            it.remove(name)
            it.remove(token)
        }
    }
}