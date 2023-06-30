package com.app.storyapp.di

import android.content.Context
import com.app.storyapp.datastore.QuoteDatabase
import com.app.storyapp.paging.QuoteRepository
import com.app.storyapp.network.ApiClient

object Injection {
    fun provideRepository(context: Context): QuoteRepository {
        val database = QuoteDatabase.getDatabase(context)
        val apiService = ApiClient.getApiService()
        return QuoteRepository(database, apiService)
    }
}