package com.example.majujayaaccessories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment

class OrderFragment : Fragment() {

    private val products = mutableListOf(
        ProductOrder(1, "Bando", "Ini adalah Bando", 10000, 5, "https://firebasestorage.googleapis.com/v0/b/realtimegps-ee77a.appspot.com/o/img_bando.png?alt=media&token=e0fecedb-6199-4cea-8b97-cf60de1c3e24"),
        ProductOrder(2, "Kalung", "Ini adalah Kalung", 10000, 5, "https://firebasestorage.googleapis.com/v0/b/realtimegps-ee77a.appspot.com/o/img_kalung.png?alt=media&token=506875e1-ec61-4acd-a431-638518722802"),
        ProductOrder(3, "Gelang", "Ini adalah Gelang", 10000, 5, "https://firebasestorage.googleapis.com/v0/b/realtimegps-ee77a.appspot.com/o/img_gelang.png?alt=media&token=a9d777a7-7bba-457a-8c96-5f8f705e1d37"),
        ProductOrder(4, "Pita", "Ini adalah Pita", 10000, 5, "https://firebasestorage.googleapis.com/v0/b/realtimegps-ee77a.appspot.com/o/img_pita.png?alt=media&token=97d6dd8d-1b3b-4a1f-837a-9a8dca83fbad"),
        ProductOrder(5, "Anting", "Ini adalah Anting", 10000, 5, "https://firebasestorage.googleapis.com/v0/b/realtimegps-ee77a.appspot.com/o/img_anting.png?alt=media&token=af79a1b7-68fb-47ab-bebc-038d7f48a8a2")
    )

    private val modifiedProducts = mutableSetOf<ProductOrder>()

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
                modifiedProducts.forEach {
                    // Print perubahan stok ke log atau tampilkan toast
                    println("Produk: ${it.productName}, Stok: ${it.stock}")
                }
                Toast.makeText(requireContext(), "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Tidak ada perubahan", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
