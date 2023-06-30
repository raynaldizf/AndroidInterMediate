package com.app.storyapp.model.response


import com.app.storyapp.model.request.Story
import com.google.gson.annotations.SerializedName

data class ResponseGetDetailStory(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("story")
    val story: Story
)