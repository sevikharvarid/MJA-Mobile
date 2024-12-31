package com.example.majujayaaccessories.admin.entity

import com.example.majujayaaccessories.response.OrderItem
import com.example.majujayaaccessories.response.User
import java.util.Date

data class StoreOrderData(
    val createdAt: Date,
    val id: Int,
    val userId: Int,
    val storeName: String,
    val storeAddress: String,
    val user: User,
    val orderItems: List<OrderItem>
)

data class StoreOrder(
    val createdAt: String,
    val storeOrder: List<StoreOrderData>
)
