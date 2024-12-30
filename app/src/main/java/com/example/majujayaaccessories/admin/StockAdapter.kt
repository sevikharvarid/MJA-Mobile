package com.example.majujayaaccessories.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.majujayaaccessories.R
import com.example.majujayaaccessories.response.ChartProduct

class StockAdapter(private val products: List<ChartProduct>) :
    RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    class StockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productTextView: TextView = view.findViewById(R.id.productTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stock, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val product = products[position]
        // Gunakan properti baru dari ChartProduct
        holder.productTextView.text = "${product.product_name} (${product.percentage_in_all_stock}%)"
    }

    override fun getItemCount() = products.size
}
