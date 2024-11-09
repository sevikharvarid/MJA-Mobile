package com.example.majujayaaccessories.api


data class RegisterResponse(
    val status_code: Int,
    val message: String,
    val data: StoreData?
)

data class StoreData(
    val id: Int,
    val user_id: Int,
    val store_name: String,
    val store_address: String,
    val is_active: Boolean,
    val created_at: String,
    val updated_at: String,
    val deleted_at: String?,
    val token: String?,
)