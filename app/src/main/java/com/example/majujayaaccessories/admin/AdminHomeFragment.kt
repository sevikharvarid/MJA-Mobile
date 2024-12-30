package com.example.majujayaaccessories.admin

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majujayaaccessories.ProductGridAdapter
import com.example.majujayaaccessories.R
import com.example.majujayaaccessories.api.ApiClient
import com.example.majujayaaccessories.databinding.FragmentAdminHomeBinding
import com.example.majujayaaccessories.response.ChartProduct
import com.example.majujayaaccessories.response.ChartResponse
import com.example.majujayaaccessories.response.Order
import com.example.majujayaaccessories.response.OrderResponse
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminHomeFragment : Fragment() {

    private var token: String? = null
    private val orderAdapter : OrderAdapter by lazy {
        OrderAdapter()
    }

    private val stockDetailAdapter : StockDetailAdapter by lazy {
        StockDetailAdapter()
    }

    private val legendStockAdapter : LegendStockAdapter by lazy {
        LegendStockAdapter()
    }

    private lateinit var binding: FragmentAdminHomeBinding
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
        binding = FragmentAdminHomeBinding.inflate(inflater)
        binding.rvOrder.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = orderAdapter
        }
        binding.rvDetailStock.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = stockDetailAdapter
        }
        binding.rvLegend.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = legendStockAdapter
        }
        fetchOrders()
        fetchStockData()
        return binding.root
    }

    private fun fetchOrders() {
        val call = ApiClient.instance.getOrders("Bearer $token")
        call.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {

                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(requireContext(), "Berhasil get", Toast.LENGTH_SHORT).show()


                    val orderList = response.body()!!.data
//                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    Log.d("FetchOrdersData", "order: $orderList")
                    val todayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val today = todayFormat.format(Date())


                    // Cek apakah ada data yang ditampilkan
                    if (orderList.isNotEmpty()) {
                        orderAdapter.setData(orderList)
                    } else {

                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Gagal mendapatkan data pesanan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                Log.d("HomeFragment", "On Failure ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
                                    stockDetailAdapter.setData(products)
                                    legendStockAdapter.setData(products)
                                } else {
                                    Log.e("FetchStockData", "Products list is null or empty")
                                    binding.pieChart.setNoDataText("No data available for Pie Chart")
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
            if (it.percentage_in_all_stock > 0) {
                with(binding){
                    pieChart.setUsePercentValues(true)
                    pieChart.isRotationEnabled = false
                    pieChart.setDrawEntryLabels(false)
                    pieChart.description.isEnabled = false
                    pieChart.isDrawHoleEnabled = false
                    pieChart.setDrawCenterText(true)
                    pieChart.animateY(1400, Easing.EaseInOutQuad)
                    pieChart.legend.isEnabled = false
                }

                Log.d("SetupPieChart", "Adding product: ${it.product_name} (${it.percentage_in_all_stock})")
                PieEntry(it.percentage.toFloat(), it.product_name)
            } else {
                Log.w("SetupPieChart", "Skipping product with zero or negative percentage: ${it.product_name}")
                null
            }
        }

        if (entries.isEmpty()) {
            Log.w("SetupPieChart", "No valid data for Pie Chart")
            binding.pieChart.setNoDataText("No valid data available for Pie Chart")
            binding.pieChart.clear()
            return
        }

        val dataSet = PieDataSet(entries, "Stock Data").apply {
            colors = listOf(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW)
        }

        val pieData = PieData(dataSet)
        binding.pieChart.data = pieData
        binding.pieChart.data.setDrawValues(false)
        binding.pieChart.invalidate()
    }
}
