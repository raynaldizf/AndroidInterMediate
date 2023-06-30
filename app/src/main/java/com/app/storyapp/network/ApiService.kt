package com.app.storyapp.network

import com.app.storyapp.model.request.Login
import com.app.storyapp.model.request.Register
import com.app.storyapp.model.response.GetAllStories
import com.app.storyapp.model.response.ListStoryItem
import com.app.storyapp.model.response.ResponseGetDetailStory
import com.app.storyapp.model.response.ResponseGetStory
import com.app.storyapp.model.response.ResponsePostStory
import com.app.storyapp.model.response.ResponseStoryHaveMap
import com.app.storyapp.model.response.ResponseUserLogin
import com.app.storyapp.model.response.ResponseUserRegister
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun register(@Body data : Register) : Call<ResponseUserRegister>

    @POST("login")
    fun login(@Body data : Login) : Call<ResponseUserLogin>

//    @GET("stories") // Implements Pagging 3 here
//    fun story(@Header("Authorization") token: String): Call<ResponseGetStory>

    @GET("stories")
    suspend fun story(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): GetAllStories

    @POST("stories")
    @Multipart
    fun uploadPhoto(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part
    ) : Call<ResponsePostStory>

    @GET("stories/{id}")
    fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ) : Call<ResponseGetDetailStory>

    @GET("stories")
    fun getStoriesByLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int
    ): Call<ResponseStoryHaveMap>

}