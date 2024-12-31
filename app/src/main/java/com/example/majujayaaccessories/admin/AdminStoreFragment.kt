package com.example.majujayaaccessories.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.majujayaaccessories.R
import com.example.majujayaaccessories.api.ApiClient
import com.example.majujayaaccessories.databinding.FragmentAdminHistoryBinding
import com.example.majujayaaccessories.databinding.FragmentAdminHomeBinding
import com.example.majujayaaccessories.databinding.FragmentAdminStoreBinding
import com.example.majujayaaccessories.response.GetStoreResponse
import com.example.majujayaaccessories.response.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminStoreFragment : Fragment() {
    private lateinit var binding: FragmentAdminStoreBinding
    private var token: String? = null
    private val storeAdapter : AdminStoreAdapter by lazy {
        AdminStoreAdapter()
    }
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

        binding = FragmentAdminStoreBinding.inflate(inflater)
        binding.rvStore.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = storeAdapter
        }
        fetchStore()
        return binding.root
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
                        storeAdapter.setData(storeList)
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