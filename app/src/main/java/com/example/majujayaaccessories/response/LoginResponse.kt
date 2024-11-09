package com.example.majujayaaccessories.response

data class LoginResponse(
    val status_code: Int, // Menambahkan status code sesuai dengan respons
    val message: String?,
    val data: UserData? // Menambahkan data pengguna sebagai UserData
)

data class UserData(
    val id: Int,
    val email: String,
    val full_name: String,
    val phone: String,
    val address: String?,
    val image_url: String?,
    val user_type: String,
    val created_at: String,
    val updated_at: String,
    val deleted_at: String?,
    val is_active: Boolean,
    val token: String,
    val store: Any? // Menggunakan Any? jika store bisa bernilai null
)
