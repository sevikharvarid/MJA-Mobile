package com.example.majujayaaccessories.response

import java.util.Date

data class Store(
    val id: Int,
    val user_id: Int,
    val store_name: String,
    val store_address: String,
    val is_active: Boolean,
    val created_at: String,
    val updated_at: String,
    val deleted_at: String?,
    val user: User
)

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val full_name: String,
    val phone: String,
    val address: String?,
    val image_url: String?,
    val user_type: String,
    val created_at: String,
    val updated_at: String,
    val deleted_at: String?,
    val is_active: Boolean,
    val token: String?
)


data class GetStoreResponse(
    val status_code: Int,
    val message: String,
    val total_data: Int,
    val data: List<Store>
)
