package com.app.storyapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.storyapp.model.request.Login
import com.app.storyapp.model.request.Register
import com.app.storyapp.model.request.Story
import com.app.storyapp.model.response.ResponseGetDetailStory
import com.app.storyapp.model.response.ResponsePostStory
import com.app.storyapp.model.response.ResponseStoryHaveMap
import com.app.storyapp.model.response.ResponseUserLogin
import com.app.storyapp.model.response.ResponseUserRegister
import com.app.storyapp.network.ApiClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelStory : ViewModel() {
    private val login = MutableLiveData<ResponseUserLogin?>()
    private val register = MutableLiveData<ResponseUserRegister?>()
    private val story = MutableLiveData<List<Story>?>()
    private val postStory = MutableLiveData<ResponsePostStory?>()
    private val detail = MutableLiveData<ResponseGetDetailStory?>()
    private val storymap = MutableLiveData<List<Story>?>()

    fun login(): MutableLiveData<ResponseUserLogin?> {
        return login
    }

    fun register(): MutableLiveData<ResponseUserRegister?> {
        return register
    }

    fun story(): MutableLiveData<List<Story>?> {
        return story
    }

    fun postStory(): MutableLiveData<ResponsePostStory?> {
        return postStory
    }

    fun detail(): MutableLiveData<ResponseGetDetailStory?> {
        return detail
    }

    fun storymap(): MutableLiveData<List<Story>?> {
        return storymap
    }

    fun loginUser(email: String, password: String) {
        ApiClient.getApiService().login(Login(email, password))
            .enqueue(object : Callback<ResponseUserLogin> {
                override fun onResponse(
                    call: Call<ResponseUserLogin>,
                    response: Response<ResponseUserLogin>
                ) {
                    if (response.isSuccessful) {
                        login.postValue(response.body())
                    } else {
                        login.postValue(null)
                    }
                }

                override fun onFailure(call: Call<ResponseUserLogin>, t: Throwable) {
                    login.postValue(null)
                }
            })
    }

    fun registerUser(name: String, email: String, password: String) {
        ApiClient.getApiService().register(Register(name, email, password))
            .enqueue(object : Callback<ResponseUserRegister> {
                override fun onResponse(
                    call: Call<ResponseUserRegister>,
                    response: Response<ResponseUserRegister>
                ) {
                    if (response.isSuccessful) {
                        register.postValue(response.body())
                    } else {
                        register.postValue(null)
                    }
                }

                override fun onFailure(call: Call<ResponseUserRegister>, t: Throwable) {
                    register.postValue(null)
                }

            })
    }

//    fun getAllStory(token: String) {
//        ApiClient.instance.story("Bearer $token").enqueue(object : Callback<ResponseGetStory> {
//            override fun onResponse(
//                call: Call<ResponseGetStory>,
//                response: Response<ResponseGetStory>
//            ) {
//                if (response.isSuccessful) {
//                    story.postValue(response.body()?.listStory)
//                } else {
//                    story.postValue(null)
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseGetStory>, t: Throwable) {
//                story.postValue(null)
//            }
//        })
//    }


    fun uploadGambar(token: String, description: RequestBody, photo: MultipartBody.Part) {
        ApiClient.getApiService().uploadPhoto("Bearer $token", description, photo)
            .enqueue(object : Callback<ResponsePostStory> {
                override fun onResponse(
                    call: Call<ResponsePostStory>,
                    response: Response<ResponsePostStory>
                ) {
                    if (response.isSuccessful) {
                        postStory.postValue(response.body())
                    } else {
                        postStory.postValue(null)
                    }
                }

                override fun onFailure(call: Call<ResponsePostStory>, t: Throwable) {
                    postStory.postValue(null)
                }

            })
    }

    fun detailStory(token: String, id: String) {
        ApiClient.getApiService().getDetailStory("Bearer $token", id)
            .enqueue(object : Callback<ResponseGetDetailStory> {
                override fun onResponse(
                    call: Call<ResponseGetDetailStory>,
                    response: Response<ResponseGetDetailStory>
                ) {
                    if (response.isSuccessful) {
                        detail.postValue(response.body())
                    } else {
                        detail.postValue(null)
                    }
                }

                override fun onFailure(call: Call<ResponseGetDetailStory>, t: Throwable) {
                    detail.postValue(null)
                }

            })
    }

    fun storyHaveMap(token: String) {
        val location = 1
        ApiClient.getApiService().getStoriesByLocation("Bearer $token", location)
            .enqueue(object : Callback<ResponseStoryHaveMap> {
                override fun onResponse(
                    call: Call<ResponseStoryHaveMap>,
                    response: Response<ResponseStoryHaveMap>
                ) {
                    if (response.isSuccessful) {
                        storymap.postValue(response.body()?.listStory)
                    } else {
                        storymap.postValue(null)
                    }
                }

                override fun onFailure(call: Call<ResponseStoryHaveMap>, t: Throwable) {
                    storymap.postValue(null)
                }
            })
    }
}