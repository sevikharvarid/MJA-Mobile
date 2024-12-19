package com.example.majujayaaccessories.request

data class OrderRequest(
    val user_id: Int,
    val order_items: List<OrderItemRequest>
)

data class OrderItemRequest(
    val product_id: Int,
    val order_quantity: Int
)