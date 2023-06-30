package com.app.storyapp.features.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.app.storyapp.adapter.QuoteListAdapter
import com.app.storyapp.model.response.ListStoryItem
import com.app.storyapp.paging.QuoteRepository
import com.app.storyapp.utils.DummyData
import com.app.storyapp.utils.MainDispatcherRule
import com.app.storyapp.utils.PagingTestDataSource
import com.app.storyapp.utils.getOrAwait
import com.app.storyapp.utils.noopListUpdateCallback
import com.app.storyapp.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repository: QuoteRepository

    @Test
    fun `check getStory not null and the the data size as expected`() = runTest {
        val dummyStory = DummyData.generateDummyStoryEntity()
        val data: PagingData<ListStoryItem> = PagingTestDataSource.snapshot(dummyStory)

        val story = MutableLiveData<PagingData<ListStoryItem>>()
        story.value = data

        Mockito.`when`(repository.getAllStories(TOKEN)).thenReturn(story)

        val viewModel = MainViewModel(repository)
        val actualStory: PagingData<ListStoryItem> = viewModel.quote(TOKEN).getOrAwait()

        val diffCallback = QuoteListAdapter.DIFF_CALLBACK

        val diff = AsyncPagingDataDiffer(
            diffCallback = diffCallback,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = Dispatchers.Main,
            workerDispatcher = Dispatchers.Main,
        )

        diff.submitData(actualStory)

        advanceUntilIdle()

        // Ensure that the data is not null
        Assert.assertNotNull(diff.snapshot())

        // Ensure that the data size is as expected
        Assert.assertEquals(dummyStory.size, diff.snapshot().size)

        // Ensure that the first item in the data matches
        Assert.assertEquals(dummyStory[0], diff.snapshot()[0])
    }

    @Test
    fun `check getStory if data is empty`() = runTest {
        val dummyStory = DummyData.generateEmptyDummyStoryEntity()
        val data: PagingData<ListStoryItem> = PagingTestDataSource.snapshot(dummyStory)

        val story = MutableLiveData<PagingData<ListStoryItem>>()
        story.value = data

        Mockito.`when`(repository.getAllStories(TOKEN)).thenReturn(story)

        val viewModel = MainViewModel(repository)
        val actualStory: PagingData<ListStoryItem> = viewModel.quote(TOKEN).getOrAwait()

        val diffCallback = QuoteListAdapter.DIFF_CALLBACK

        val diff = AsyncPagingDataDiffer(
            diffCallback = diffCallback,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = Dispatchers.Main,
            workerDispatcher = Dispatchers.Main,
        )

        diff.submitData(actualStory)

        advanceUntilIdle()

        // Ensure that the data is not null
        Assert.assertNotNull(diff.snapshot())

        // Ensure that the data size is zero
        Assert.assertEquals(0, diff.snapshot().size)
    }

    companion object {
        private const val TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWNxLWpjaDRvUEMyVExyMXIiLCJpYXQiOjE2ODgxMTAyNjd9.YMkvgYstWRDQi_txyDkZYRXyiSEKf1WVEz7Gk-9yp8A"
    }
}
