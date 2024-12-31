package com.example.majujayaaccessories.response

import java.util.Date

data class Product(
    val id: Int,
    val product_name: String,
    val product_description: String,
    val price: Int,
    val stock: Int,
    val image_url: String,
    val is_active: Boolean
)

data class OrderItem(
    val id: Int,
    val order_id: Int,
    val product_id: Int,
    val order_quantity: Int,
    val is_active: Boolean,
    val created_at: Date,
    val product: Product
)

data class Order(
    val id: Int,
    val user_id: Int,
    val store_id: Int,
    val is_active: Boolean,
    val created_at: Date,
    val order_items: List<OrderItem>
)

data class OrderResponse(
    val status_code: Int,
    val message: String,
    val total_data: Int,
    val data: List<Order>
)

data class OrderHistory(
    val createdAt: String,
    val orderItems: List<OrderItem>
)

