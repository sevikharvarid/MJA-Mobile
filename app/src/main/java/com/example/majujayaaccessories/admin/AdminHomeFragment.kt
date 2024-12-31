package com.example.majujayaaccessories.admin

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.majujayaaccessories.R
import com.example.majujayaaccessories.admin.entity.ChartDataSet
import com.example.majujayaaccessories.admin.entity.StoreOrderData
import com.example.majujayaaccessories.api.ApiClient
import com.example.majujayaaccessories.databinding.FragmentAdminHomeBinding
import com.example.majujayaaccessories.response.ChartProduct
import com.example.majujayaaccessories.response.ChartResponse
import com.example.majujayaaccessories.response.GetStoreResponse
import com.example.majujayaaccessories.response.Order
import com.example.majujayaaccessories.response.OrderResponse
import com.example.majujayaaccessories.response.Store
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
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

//    private val dataStoreOrder = mutableListOf<StoreOrderData>()
    private val dataOrder = mutableListOf<Order>()
    private val dataStore = mutableListOf<Store>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString("token")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        binding.tvSeeDetail.setOnClickListener {
            val orderFragment = AdminOrderFragment().apply {
                arguments = Bundle().apply {
                    putString("token", token)
                }
            }
            loadFragment(orderFragment)
        }
        fetchOrders()
        fetchStockData()
        return binding.root
    }

    private fun loadFragment(fragment: Fragment) {
        val sharedPreferences = requireActivity().getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token != null) {
            fragment.arguments = Bundle().apply {
                putString("token", token)
            }
        } else {
            Log.e("AdminDashboardActivity", "Token tidak ditemukan, fragment tidak dimuat.")
            // Redirect ke layar login jika token tidak ditemukan
        }


            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()

    }

    private fun fetchStore() {
        val call = ApiClient.instance.getStores("Bearer $token")
        call.enqueue(object : Callback<GetStoreResponse> {
            override fun onResponse(call: Call<GetStoreResponse>, response: Response<GetStoreResponse>) {

                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(requireContext(), "Get Store Data", Toast.LENGTH_SHORT).show()


                    val storeList = response.body()!!.data
                    Log.d("FetchStoreData", "order: $storeList")

                    if (storeList.isNotEmpty()) {
                        dataStore.clear()
                        dataStore.addAll(storeList)

                        val dataList = dataOrder.mapNotNull { order ->
                            val matchingStore = dataStore.find { it.id == order.store_id }
                            matchingStore?.let { StoreOrderData(
                                createdAt = order.created_at,
                                id = it.id,
                                userId = it.user_id,
                               storeName =  it.store_name,
                                storeAddress = it.store_address,
                                user = it.user,
                                orderItems = order.order_items
                            ) }
                        }

                        Log.d("dataStoreOrder","dataList $dataList")

                        orderAdapter.setData(dataList)
                    }
                    else{
                        Toast.makeText(requireContext(), "Tidak ada toko", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Gagal mendapatkan data toko",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<GetStoreResponse>, t: Throwable) {
                Log.d("StorePage", "On Failure ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun fetchOrders() {
        val call = ApiClient.instance.getOrders("Bearer $token")
        call.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {

                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(requireContext(), "Berhasil get", Toast.LENGTH_SHORT).show()


                    val orderList = response.body()!!.data
                    Log.d("FetchOrdersData", "order: $orderList")
                    val todayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val today = todayFormat.format(Date())

                    if (orderList.isNotEmpty()) {
                        dataOrder.clear()
                        dataOrder.addAll(orderList.filter { item ->
                            todayFormat.format(item.created_at) == today
                        }.take(3))
                        fetchStore()
                    }
                    else{
                        Toast.makeText(requireContext(), "Tidak ada pesanan hari ini", Toast.LENGTH_SHORT).show()
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
                                val products = responseData.data.products
                                if (products.isNotEmpty()) {
                                    Log.d("FetchStockData", "Products: $products")
                                    setupPieChart(products)
                                    stockDetailAdapter.setData(products)
//                                    legendStockAdapter.setData(products)
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

        val dynamicColors = entries.map {
            Color.rgb(
                (0..255).random(),
                (0..255).random(),
                (0..255).random()
            )
        }
        val dataSet = PieDataSet(entries, "Stock Data").apply {
            colors = dynamicColors
        }
        val pieData = PieData(dataSet)
        binding.pieChart.data = pieData
        binding.pieChart.data.setDrawValues(false)
        val legendItems = mutableListOf<ChartDataSet>()

        val colors = dataSet.colors // Ambil warna
        val labels = entries.map { it.label } // Ambil label
        val values = entries.map { it.value }
        labels.zip(values) { label, value ->
            val color = colors[labels.indexOf(label)]
            legendItems.add(ChartDataSet(label, color, value))
        }

        legendStockAdapter.setData(legendItems)

        binding.pieChart.invalidate()
    }
}
