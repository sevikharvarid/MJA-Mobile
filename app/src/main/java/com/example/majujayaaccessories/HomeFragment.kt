package com.example.majujayaaccessories

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.majujayaaccessories.api.ApiClient
import com.example.majujayaaccessories.models.ProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var token: String? = null

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Ambil referensi ke GridView
        val productGridView: GridView = view.findViewById(R.id.productGridView)

        // Panggil API untuk mengambil produk
        if (token != null) {
            fetchProducts(productGridView, token!!)
        } else {
            Toast.makeText(requireContext(), "Token tidak tersedia. Silakan login kembali.", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun fetchProducts(productGridView: GridView, token: String) {
        Log.d("HomeFragment", "Token: $token")
        val call = ApiClient.instance.getProducts("Bearer $token")
        Log.d("HomeFragment", "Calling API...")
        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                Log.d("HomeFragment", "API Response: ${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val productList = response.body()!!.data
                    productGridView.adapter = ProductGridAdapter(requireContext(), productList)
                } else {
                    Log.e("HomeFragment", "Gagal mendapatkan produk: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Gagal mendapatkan data produk", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.e("HomeFragment", "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
