package com.example.majujayaaccessories.api

import com.example.majujayaaccessories.request.LoginRequest
import com.example.majujayaaccessories.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// ApiService.kt
interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
