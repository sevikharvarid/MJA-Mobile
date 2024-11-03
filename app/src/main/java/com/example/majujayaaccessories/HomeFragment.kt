package com.example.majujayaaccessories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import org.json.JSONObject

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Ambil referensi ke GridView
        val productGridView: GridView = view.findViewById(R.id.productGridView)

        // Contoh respons JSON
        val jsonResponse = """{
            "status_code": 200,
            "message": "Success get products",
            "total_data": 3,
            "data": [
                {
                    "id": 1,
                    "product_name": "Kopi susu ABC",
                    "product_description": "Ini adalah kopi susu ABC",
                    "price": 100000,
                    "stock": 10,
                    "image_url": "",
                    "is_active": true,
                    "created_at": "2024-10-29T16:04:09.589Z",
                    "updated_at": "2024-10-29T16:04:09.589Z",
                    "deleted_at": null
                },
                {
                    "id": 2,
                    "product_name": "Kopi tubruk orang",
                    "product_description": "Ini adalah kopi tubruk orang",
                    "price": 200000,
                    "stock": 20,
                    "image_url": "",
                    "is_active": true,
                    "created_at": "2024-10-29T16:04:09.589Z",
                    "updated_at": "2024-10-29T16:04:09.589Z",
                    "deleted_at": null
                },
                {
                    "id": 3,
                    "product_name": "Kopi kapal lawd",
                    "product_description": "Ini adalah kopi kapal lawd",
                    "price": 400000,
                    "stock": 40,
                    "image_url": "",
                    "is_active": true,
                    "created_at": "2024-10-29T16:04:09.589Z",
                    "updated_at": "2024-10-29T16:04:09.589Z",
                    "deleted_at": null
                }
            ]
        }"""

        val productList = parseJsonResponse(jsonResponse)
        productGridView.adapter = ProductGridAdapter(requireContext(), productList)

        return view
    }

    private fun parseJsonResponse(jsonResponse: String): List<Product> {
        val gson = Gson()
        val jsonObject = JSONObject(jsonResponse)
        val dataArray = jsonObject.getJSONArray("data")

        return (0 until dataArray.length()).map { i ->
            val productJson = dataArray.getJSONObject(i)
            Product(
                id = productJson.getInt("id"),
                productName = productJson.getString("product_name"),
                productDescription = productJson.getString("product_description"),
                price = productJson.getInt("price"),
                stock = productJson.getInt("stock"),
                imageUrl = productJson.getString("image_url"),
                isActive = productJson.getBoolean("is_active")
            )
        }
    }
}
