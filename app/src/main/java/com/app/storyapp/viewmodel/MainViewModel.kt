package com.app.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.storyapp.model.response.ListStoryItem
import com.app.storyapp.paging.QuoteRepository


class MainViewModel(private val quoteRepository: QuoteRepository) : ViewModel() {

    fun quote(token : String): LiveData<PagingData<ListStoryItem>> =
        quoteRepository.getAllStories(token).cachedIn(viewModelScope)

}