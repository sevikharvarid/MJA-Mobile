package com.example.majujayaaccessories.response

// UserUpdateRequest.kt
data class UserUpdateRequest(
    var email: String,
    var full_name: String,
    var phone: String,
    var password: String,
    var image_url: String
)