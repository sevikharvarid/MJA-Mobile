package com.example.majujayaaccessories.api

import com.example.majujayaaccessories.models.ProductResponse
import com.example.majujayaaccessories.request.LoginRequest
import com.example.majujayaaccessories.request.OrderRequest
import com.example.majujayaaccessories.request.StoreRequest
import com.example.majujayaaccessories.response.ChartResponse
import com.example.majujayaaccessories.response.GetStoreResponse
import com.example.majujayaaccessories.response.LoginResponse
import com.example.majujayaaccessories.response.OrderResponse
import com.example.majujayaaccessories.response.StoreResponse
import com.example.majujayaaccessories.response.UserProfileResponse
import com.example.majujayaaccessories.response.UserUpdateRequest
import com.example.majujayaaccessories.response.UserUpdateResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// ApiService.kt
interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Tidak jadi di pakai
//    @GET("api/v1/products")
//    fun getProducts(
//        @Header("Authorization") token: String,
//        @Query("limit") limit: Int = 10,
//        @Query("offset") offset: Int = 0
//    ): Call<ProductResponse>
    @GET("api/v1/orders")
    fun getOrders(
        @Header("Authorization") token: String
    ): Call<OrderResponse>

    @POST("api/v1/orders")
    @Headers("Content-Type: application/json")
    fun createOrder(
        @Header("Authorization") token: String,
        @Body orderRequest: OrderRequest
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/register")
    suspend fun registerUser(@Body requestBody: RegisterRequest): Response<RegisterResponse>

    @POST("api/v1/stores")
    suspend fun registerStore(
        @Header("Authorization") token: String,
        @Body storeRequest: StoreRequest
    ): Response<RegisterResponse>

    @GET("api/v1/auth/users/{id}")
    fun getUserProfile(
        @Path("id") userId: Int,
        @Header("Authorization") authToken: String
    ): Call<UserProfileResponse>

    @PUT("api/v1/auth/users/{id}")
    fun updateUser(
        @Path("id") id: Int,
        @Body request: UserUpdateRequest,
        @Header("Authorization") authToken: String
    ): Call<UserUpdateResponse>

    @GET("api/v1/reports/stocks")
    fun getStockReports(@Header("Authorization") token: String): Call<ChartResponse>

    @GET("api/v1/stores")
    fun getStores(
        @Header("Authorization") token: String
    ): Call<GetStoreResponse>

}
