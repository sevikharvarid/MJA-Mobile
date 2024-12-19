package com.example.majujayaaccessories

data class UserProfile(
    val full_name: String,
    val email: String,
    val image_url: String,
    val phone: String,
    val address: String,
    val store: UserStore?
)

data class UserStore(
    val store_name: String,
    val store_address: String
)
