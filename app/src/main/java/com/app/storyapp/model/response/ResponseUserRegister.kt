package com.app.storyapp.model.response


import com.google.gson.annotations.SerializedName

data class ResponseUserRegister(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)