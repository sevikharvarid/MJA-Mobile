package com.example.majujayaaccessories

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
import com.example.majujayaaccessories.api.ApiClient
import com.example.majujayaaccessories.response.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var token: String? = null
    private var userId: Int? = null

    private lateinit var loadingDialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString("token")
            userId = it.getString("userId")?.toInt()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val productGridView: GridView = view.findViewById(R.id.productGridView)
        val emptyImageView: ImageView = view.findViewById(R.id.emptyImageView)
        val emptyTextView: TextView = view.findViewById(R.id.emptyTextView)



        if (token != null) {
            fetchOrders(productGridView,emptyImageView, emptyTextView,token!!, userId!!)
        } else {
            Toast.makeText(
                requireContext(),
                "Token tidak tersedia. Silakan login kembali.",
                Toast.LENGTH_SHORT
            ).show()
        }

        return view
    }

    private fun fetchOrders(
        productGridView: GridView,
        emptyImageView: ImageView,
        emptyTextView: TextView,
        token: String, userId: Int
    ) {
        // Tampilkan dialog loading
        showLoadingDialog()

        val call = ApiClient.instance.getOrders("Bearer $token")
        call.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                hideLoadingDialog()

                if (response.isSuccessful && response.body() != null) {

                    // Sembunyikan dialog loading

                    Toast.makeText(requireContext(), "Berhasil get", Toast.LENGTH_SHORT).show()


                    val orderList = response.body()!!.data
//                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                    val todayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val today = todayFormat.format(Date())

                    val filteredItems = orderList.flatMap { order ->
                        // Pastikan user_id cocok dengan userId
                        if (order.user_id == userId) {
                            order.order_items.filter { item ->
                                // Format created_at menjadi String dengan format yang sama dengan today
                                todayFormat.format(item.created_at) == today
                            }
                        } else {
                            emptyList() // Kembalikan list kosong jika user_id tidak sesuai
                        }
                    }


                    Log.d("HomeFragment", "On response ${filteredItems}")

                    // Cek apakah ada data yang ditampilkan
                    if (filteredItems.isNotEmpty()) {
                        productGridView.adapter =
                            ProductGridAdapter(requireContext(), filteredItems)
                        emptyImageView.visibility = View.GONE
                        emptyTextView.visibility = View.GONE
                    } else {
                        emptyImageView.visibility = View.VISIBLE
                        emptyTextView.visibility = View.VISIBLE
                        productGridView.adapter = null // Kosongkan adapter jika tidak ada data
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
                // Sembunyikan dialog loading
                hideLoadingDialog()
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoadingDialog() {
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setView(R.layout.loading_dialog)
        builder.setCancelable(false) // Membuat dialog tidak bisa ditutup dengan back button
        loadingDialog = builder.create()
        loadingDialog.show()
    }

    private fun hideLoadingDialog() {
        if (::loadingDialog.isInitialized) {
            loadingDialog.dismiss()
        }
    }

}
