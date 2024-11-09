package com.example.majujayaaccessories.models

data class Product(
    val id: Int,
    val product_name: String,
    val productDescription: String,
    val price: Int,
    val stock: Int,
    val image_url: String,
    val isActive: Boolean
)

data class ProductResponse(
    val status_code: Int,
    val message: String,
    val total_data: Int,
    val data: List<Product>
)
