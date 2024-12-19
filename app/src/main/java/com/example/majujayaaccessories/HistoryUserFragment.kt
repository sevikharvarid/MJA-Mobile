package com.example.majujayaaccessories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majujayaaccessories.api.ApiClient
import com.example.majujayaaccessories.response.OrderHistory
import com.example.majujayaaccessories.response.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment() {

    private var token: String? = null
    private var userId: Int? = null
    private lateinit var loadingDialog: android.app.AlertDialog
    private lateinit var historyUserAdapter: HistoryUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString("token")
            userId = it.getString("userId")?.toInt()
        }
        Log.d("HistoryFragment", "onCreate: token=$token, userId=$userId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history_user, container, false)
        Log.d("HistoryFragment", "onCreateView: View initialized")

        val recyclerView: RecyclerView = view.findViewById(R.id.verticalRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        historyUserAdapter = HistoryUserAdapter(requireContext(), emptyList())
        recyclerView.adapter = historyUserAdapter

        Log.d("HistoryFragment", "Adapter set to RecyclerView")

        if (token != null) {
            Log.d("HistoryFragment", "Token is available, fetching orders")
            fetchOrders(token!!, userId!!)
        } else {
            Log.e("HistoryFragment", "Token is null, showing error message")
            Toast.makeText(
                requireContext(),
                "Token tidak tersedia. Silakan login kembali.",
                Toast.LENGTH_SHORT
            ).show()
        }

        return view
    }

    private fun fetchOrders(token: String, userId: Int) {
        Log.d("HistoryFragment", "Fetching orders: token=$token, userId=$userId")

        showLoadingDialog()

        val call = ApiClient.instance.getOrders("Bearer $token")
        call.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                Log.d("HistoryFragment", "onResponse: Received response")

                hideLoadingDialog()

                if (response.isSuccessful && response.body() != null) {
                    Log.d("HistoryFragment", "onResponse: Response successful")

                    val orderList = response.body()!!.data
                    Log.d("HistoryFragment", "onResponse: Order list size=${orderList.size}")

                    // Kelompokkan berdasarkan tanggal `created_at`
                    val groupedOrders = orderList
                        .filter { it.user_id == userId }
                        .groupBy { order ->
                            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            format.format(order.created_at)
                        }
                        .map { (createdAt, orders) ->
                            OrderHistory(
                                createdAt = createdAt,
                                orderItems = orders.flatMap { it.order_items }
                            )
                        }

                    Log.d("HistoryFragment", "onResponse: Grouped orders=$groupedOrders")

//                     Perbarui data adapter
                    if (groupedOrders.isNotEmpty()) {
                        historyUserAdapter.updateList(groupedOrders)
                        Log.d("HistoryFragment", "onResponse: Updated adapter with grouped orders")
                    } else {
                        Log.w("HistoryFragment", "onResponse: No orders found")
                        Toast.makeText(requireContext(), "Tidak ada data pesanan", Toast.LENGTH_SHORT).show()
                    }
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
                hideLoadingDialog()
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoadingDialog() {
        Log.d("HistoryFragment", "Showing loading dialog")
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setView(R.layout.loading_dialog)
        builder.setCancelable(false)
        loadingDialog = builder.create()
        loadingDialog.show()
    }

    private fun hideLoadingDialog() {
        Log.d("HistoryFragment", "Hiding loading dialog")
        if (::loadingDialog.isInitialized) {
            loadingDialog.dismiss()
        }
    }
}
