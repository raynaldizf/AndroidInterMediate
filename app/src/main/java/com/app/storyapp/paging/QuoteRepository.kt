package com.app.storyapp.paging

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.app.storyapp.datastore.QuoteDatabase
import com.app.storyapp.model.response.ListStoryItem
import com.app.storyapp.network.ApiService

class QuoteRepository(private val storyDatabase: QuoteDatabase,private val apiInterface: ApiService) {
    fun getAllStories(token:String):LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                QuotePagingSource(apiInterface,token)
            }
        ).liveData
    }

}