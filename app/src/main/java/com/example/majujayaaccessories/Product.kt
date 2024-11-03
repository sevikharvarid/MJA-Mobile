package com.example.majujayaaccessories

data class Product(
    val id: Int,
    val productName: String,
    val productDescription: String,
    val price: Int,
    val stock: Int,
    val imageUrl: String,
    val isActive: Boolean
)
