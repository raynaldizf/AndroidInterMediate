package com.app.storyapp.utils

import com.app.storyapp.model.response.ListStoryItem
import com.app.storyapp.model.response.LoginResult
import com.app.storyapp.model.response.ResponseUserRegister

object DummyData {

    fun generateDummyStoryEntity(): List<ListStoryItem> {
        val storyList: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..50) {
            val story = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "created + $i",
                "story-$i",
                "description + $i",
                i.toDouble(),
                i.toString(),
                i.toDouble(),
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateEmptyDummyStoryEntity() : List<ListStoryItem> {
        return emptyList()
    }
    fun generateDummyStoryWithLocationEntity(): List<ListStoryItem> {
        val storyList: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..50) {
            val story = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "created + $i",
                "story-$i",
                "description + $i",
                i.toDouble(),
                i.toString(),
                i.toDouble(),
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateDummyLoginEntity() = LoginResult(
            "name",
            "id",
            "token"
    )

    fun generateDummyRegisterEntity() = ResponseUserRegister(
        false,
        "success"
    )

}