package com.sulton.belibijak.data.remote

import retrofit2.Call
import retrofit2.http.*

const val myApiKey = "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"
interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token $myApiKey")
    fun getUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $myApiKey")
    fun detailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @POST("login")
    fun reqLogin(@Body signing: SignIn): Call<LogResponse>


}