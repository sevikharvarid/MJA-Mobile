package com.example.majujayaaccessories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.example.majujayaaccessories.response.OrderItem

class ProductGridAdapter(
    private val context: Context,
    private val orderItems: List<OrderItem>
) : BaseAdapter() {

    override fun getCount(): Int = orderItems.size

    override fun getItem(position: Int): Any = orderItems[position]

    override fun getItemId(position: Int): Long = orderItems[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)

        val productImage = view.findViewById<ImageView>(R.id.productImageView)
        val productName = view.findViewById<TextView>(R.id.productNameTextView)

        val item = orderItems[position]

        // Set data produk
        productName.text = item.product.product_name

        // Gunakan Glide untuk memuat gambar produk
        Glide.with(context)
            .load(item.product.image_url)
            .into(productImage)

        return view
    }
}
