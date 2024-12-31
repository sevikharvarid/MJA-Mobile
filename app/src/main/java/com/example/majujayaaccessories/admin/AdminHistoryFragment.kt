package com.example.majujayaaccessories.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majujayaaccessories.admin.entity.StoreOrder
import com.example.majujayaaccessories.admin.entity.StoreOrderData
import com.example.majujayaaccessories.api.ApiClient
import com.example.majujayaaccessories.databinding.FragmentAdminHistoryBinding
import com.example.majujayaaccessories.response.GetStoreResponse
import com.example.majujayaaccessories.response.Order
import com.example.majujayaaccessories.response.OrderResponse
import com.example.majujayaaccessories.response.Store
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class AdminHistoryFragment : Fragment() {
    private lateinit var binding: FragmentAdminHistoryBinding
    private var token: String? = null
    private val dataOrder = mutableListOf<Order>()
    private val dataStore = mutableListOf<Store>()
    private lateinit var historyUserAdapter: OrderAdminAdapter

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
        // Inflate the layout for this fragment
        binding = FragmentAdminHistoryBinding.inflate(inflater)

        val recyclerView: RecyclerView = binding.rvHistory
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        historyUserAdapter = OrderAdminAdapter(requireContext(), emptyList())
        recyclerView.adapter = historyUserAdapter
        fetchOrders(token.toString())
        return binding.root
    }

    private fun fetchOrders(token: String) {
        Log.d("HistoryFragment", "Fetching orders: token=$token")


        val call = ApiClient.instance.getOrders("Bearer $token")
        call.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                Log.d("HistoryFragment", "onResponse: Received response")

                if (response.isSuccessful && response.body() != null) {
                    Log.d("HistoryFragment", "onResponse: Response successful")

                    val orderList = response.body()!!.data
                    Log.d("HistoryFragment", "onResponse: Order list size=${orderList.size}")

                    dataOrder.addAll(orderList)
                    fetchStore()

                } else {
                    Log.e("HistoryFragment", "onResponse: Failed to get orders, responseCode=${response.code()}")
                    Toast.makeText(
                        requireContext(),
                        "Gagal mendapatkan data pesanan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                Log.e("HistoryFragment", "onFailure: ${t.message}", t)
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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

                        val groupStoreOrder = dataList
                            .groupBy { storeOrder ->
                                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                format.format(storeOrder.createdAt)
                            }
                            .map { (createdAt, orders) ->
                                StoreOrder(
                                    createdAt = createdAt,
                                    storeOrder = orders
                                )
                            }

                        Log.d("HistoryFragment","dataList $dataList")

                        historyUserAdapter.updateList(groupStoreOrder)
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


}