package com.app.storyapp.model.response


import com.google.gson.annotations.SerializedName

data class ResponseUserLogin(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("loginResult")
    val loginResult: LoginResult,
    @SerializedName("message")
    val message: String
)