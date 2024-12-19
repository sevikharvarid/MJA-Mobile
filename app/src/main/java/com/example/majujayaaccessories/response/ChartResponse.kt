package com.example.majujayaaccessories.response

data class ChartResponse(
    val statusCode: Int,
    val message: String,
    val data: ChartData
)

data class ChartData(
    val products: List<ChartProduct>,
    val totalProducts: Int,
    val totalStock: Int
)

data class ChartProduct(
    val productId: Int,
    val productName: String,
    val currentStock: Int,
    val totalOrderQuantity: Int,
    val totalStock: Int,
    val percentage: Double,
    val percentageInAllStock: Double
)
