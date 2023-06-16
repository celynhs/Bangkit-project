package com.sulton.belibijak.data.remote

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

const val myApiKey = "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"
interface ApiService {

    @POST("auth/signin")
    fun signIn(@Body signing: SignIn): Call<AuthResponse>
    @POST("auth/signup")
    fun signUp(@Body signing: SignUp): Call<ResponseReg>

    @POST("recommendations")
    suspend fun getRecommendation(
        @Body budgetPost: BudgetPost ) : Response<RecommendationsResponse>


}