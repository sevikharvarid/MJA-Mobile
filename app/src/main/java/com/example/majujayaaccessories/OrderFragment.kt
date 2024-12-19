package com.example.majujayaaccessories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.majujayaaccessories.api.ApiClient
import com.example.majujayaaccessories.request.OrderItemRequest
import com.example.majujayaaccessories.request.OrderRequest
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderFragment : Fragment() {

    private val products = mutableListOf(
        ProductOrder(1, "Anting", "Ini adalah Anting", 10000, 0, "https://firebasestorage.googleapis.com/v0/b/realtimegps-ee77a.appspot.com/o/img_anting.png?alt=media&token=af79a1b7-68fb-47ab-bebc-038d7f48a8a2"),
        ProductOrder(2, "Kalung", "Ini adalah Kalung", 10000, 0, "https://firebasestorage.googleapis.com/v0/b/realtimegps-ee77a.appspot.com/o/img_kalung.png?alt=media&token=506875e1-ec61-4acd-a431-638518722802"),
        ProductOrder(3, "Gelang", "Ini adalah Gelang", 10000, 0, "https://firebasestorage.googleapis.com/v0/b/realtimegps-ee77a.appspot.com/o/img_gelang.png?alt=media&token=a9d777a7-7bba-457a-8c96-5f8f705e1d37"),
        ProductOrder(4, "Bando", "Ini adalah Bando", 10000, 0, "https://firebasestorage.googleapis.com/v0/b/realtimegps-ee77a.appspot.com/o/img_bando.png?alt=media&token=e0fecedb-6199-4cea-8b97-cf60de1c3e24"),
        ProductOrder(5, "Pita", "Ini adalah Pita", 10000, 0, "https://firebasestorage.googleapis.com/v0/b/realtimegps-ee77a.appspot.com/o/img_pita.png?alt=media&token=97d6dd8d-1b3b-4a1f-837a-9a8dca83fbad")
    )

    private val modifiedProducts = mutableSetOf<ProductOrder>()

    private lateinit var loadingDialog: android.app.AlertDialog

    private var token: String? = null
    private var userId: Int? = null

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
        val view = inflater.inflate(R.layout.fragment_order, container, false)
        val gridView: GridView = view.findViewById(R.id.gridViewProducts)
        val btnConfirm: Button = view.findViewById(R.id.btnConfirm)

        val adapter = ProductAdapter(requireContext(), products) { product ->
            modifiedProducts.add(product)
        }
        gridView.adapter = adapter

        btnConfirm.setOnClickListener {
            if (modifiedProducts.isNotEmpty()) {
                // Show loading
                showLoadingDialog()
                btnConfirm.isEnabled = false
                btnConfirm.text = "Loading..."

                val id = userId // Set the user_id as needed
                val orderItems = modifiedProducts.map { product ->
                    OrderItemRequest(product_id = product.id, order_quantity = product.stock)
                }
                val orderRequest = OrderRequest(user_id = id!!, order_items = orderItems)



                // Make API call
                ApiClient.instance.createOrder("Bearer $token", orderRequest)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            btnConfirm.isEnabled = true
                            btnConfirm.text = "Confirm Order"

                            Log.d("OrderFragment","Response $response")


                            if (response.isSuccessful) {
                                // Reset stock to 0
                                modifiedProducts.forEach { it.stock = 0 }
                                adapter.notifyDataSetChanged()
                                Toast.makeText(requireContext(), "Order placed successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), "Failed to place order", Toast.LENGTH_SHORT).show()
                            }
                            hideLoadingDialog()
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            btnConfirm.isEnabled = true
                            btnConfirm.text = "Confirm Order"
                            hideLoadingDialog()

                            Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                Toast.makeText(requireContext(), "No changes made", Toast.LENGTH_SHORT).show()
            }
        }

        return view
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
