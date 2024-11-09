package com.example.majujayaaccessories.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// ApiClient.kt
object ApiClient {
    private const val BASE_URL = "https://mja-backend.vercel.app/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }


}
