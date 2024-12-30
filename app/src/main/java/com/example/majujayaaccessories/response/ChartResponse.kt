package com.example.majujayaaccessories.response

data class ChartResponse(
    val status_code: Int,
    val message: String,
    val data: ChartData
)

data class ChartData(
    val products: List<ChartProduct>,
    val total_products: Int,
    val total_stock: Int
)

data class ChartProduct(
    val product_id: Int,
    val product_name: String,
    val current_stock: Int,
    val total_order_quantity: Int,
    val total_stock: Int,
    val percentage: Double,
    val percentage_in_all_stock: Double
)
