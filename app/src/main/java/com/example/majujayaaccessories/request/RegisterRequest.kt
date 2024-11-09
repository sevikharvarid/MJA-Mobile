package com.example.majujayaaccessories.api

data class RegisterRequest(
    val full_name: String,
    val phone: String,
    val email: String,
    val password: String,
    val user_type: String = "user"
)