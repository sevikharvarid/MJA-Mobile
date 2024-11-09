package com.example.majujayaaccessories.api

import com.example.majujayaaccessories.models.ProductResponse
import com.example.majujayaaccessories.request.LoginRequest
import com.example.majujayaaccessories.request.StoreRequest
import com.example.majujayaaccessories.response.LoginResponse
import com.example.majujayaaccessories.response.StoreResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

// ApiService.kt
interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/v1/products")
    fun getProducts(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): Call<ProductResponse>

    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/register")
    suspend fun registerUser(@Body requestBody: RegisterRequest): Response<RegisterResponse>

    @POST("api/v1/stores")
    suspend fun registerStore(
        @Header("Authorization") token: String,
        @Body storeRequest: StoreRequest
    ): Response<RegisterResponse>

}
