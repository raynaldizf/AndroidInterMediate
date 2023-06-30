package com.app.storyapp.model.response


import com.app.storyapp.model.request.Story
import com.google.gson.annotations.SerializedName

data class ResponseGetStory(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStory: List<Story>,
    @SerializedName("message")
    val message: String
)