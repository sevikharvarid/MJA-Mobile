package com.example.majujayaaccessories.response

import com.example.majujayaaccessories.UserProfile

data class UserProfileResponse(
    val status_code: Int,
    val message: String,
    val data: UserProfile
)
