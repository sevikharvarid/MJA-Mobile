package com.example.majujayaaccessories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.majujayaaccessories.response.OrderItem

class HistoryItemAdapter(
    private val context: Context,
    private val items: List<OrderItem>
) : RecyclerView.Adapter<HistoryItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
        val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val productQuantityTextView: TextView = itemView.findViewById(R.id.productQuantityTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        // Set nama produk
        holder.productNameTextView.text = item.product.product_name

        // Set jumlah produk
        holder.productQuantityTextView.text = "Jumlah: ${item.order_quantity}"

        // Load gambar produk
        Glide.with(context)
            .load(item.product.image_url) // URL atau resource gambar
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(holder.productImageView)
    }

    override fun getItemCount(): Int = items.size
}
