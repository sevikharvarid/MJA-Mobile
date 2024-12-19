package com.example.majujayaaccessories.admin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majujayaaccessories.R
import com.example.majujayaaccessories.api.ApiClient
import com.example.majujayaaccessories.response.ChartProduct
import com.example.majujayaaccessories.response.ChartResponse
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminHomeFragment : Fragment() {

    private var token: String? = null
    private lateinit var pieChart: PieChart
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString("token")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)

        pieChart = view.findViewById(R.id.pieChart)
        recyclerView = view.findViewById(R.id.recyclerView)

        fetchStockData()

        return view
    }

    private fun fetchStockData() {
        token?.let { tokenValue ->
            val formattedToken = "Bearer $tokenValue"
            Log.d("FetchStockData", "Authorization header: $formattedToken")

            ApiClient.instance.getStockReports(formattedToken)
                .enqueue(object : Callback<ChartResponse> {
                    override fun onResponse(
                        call: Call<ChartResponse>,
                        response: Response<ChartResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseData = response.body()
                            if (responseData != null) {
                                val products = responseData.data?.products
                                if (!products.isNullOrEmpty()) {
                                    Log.d("FetchStockData", "Products: $products")
                                    setupPieChart(products)
                                    setupRecyclerView(products)
                                } else {
                                    Log.e("FetchStockData", "Products list is null or empty")
                                    pieChart.setNoDataText("No data available for Pie Chart")
                                }
                            } else {
                                Log.e("FetchStockData", "Response body is null")
                                Toast.makeText(context, "No data received", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e(
                                "FetchStockData",
                                "Failed: ${response.code()} - ${response.message()} - ${response.errorBody()?.string()}"
                            )
                            Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ChartResponse>, t: Throwable) {
                        Log.e("FetchStockData", "Error: ${t.message}", t)
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } ?: run {
            Log.e("FetchStockData", "Token is missing")
            Toast.makeText(context, "Token is missing", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupPieChart(products: List<ChartProduct>) {
        Log.d("SetupPieChart", "Number of products: ${products.size}")
        val entries = products.mapNotNull {
            if (it.percentageInAllStock > 0) {
                Log.d("SetupPieChart", "Adding product: ${it.productName} (${it.percentageInAllStock})")
                PieEntry(it.percentageInAllStock.toFloat(), it.productName)
            } else {
                Log.w("SetupPieChart", "Skipping product with zero or negative percentage: ${it.productName}")
                null
            }
        }

        if (entries.isEmpty()) {
            Log.w("SetupPieChart", "No valid data for Pie Chart")
            pieChart.setNoDataText("No valid data available for Pie Chart")
            pieChart.clear()
            return
        }

        val dataSet = PieDataSet(entries, "Stock Data").apply {
            colors = listOf(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW)
        }

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.invalidate()
    }

    private fun setupRecyclerView(products: List<ChartProduct>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = StockAdapter(products)
    }
}
