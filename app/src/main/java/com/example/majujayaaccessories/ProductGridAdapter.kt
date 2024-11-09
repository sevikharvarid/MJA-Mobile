package com.example.majujayaaccessories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class ProductGridAdapter(
    private val context: Context,
    private val productList: List<com.example.majujayaaccessories.models.Product>
) : BaseAdapter() {

    override fun getCount(): Int = productList.size

    override fun getItem(position: Int): Any = productList[position]

    override fun getItemId(position: Int): Long = productList[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)

        val product = getItem(position) as com.example.majujayaaccessories.models.Product
        val productImageView: ImageView = view.findViewById(R.id.productImageView)
        val productNameTextView: TextView = view.findViewById(R.id.productNameTextView)
        val productQuantityTextView: TextView = view.findViewById(R.id.productQuantityTextView)

        // Check if productName is null, if so, set default text
        productNameTextView.text = product.product_name ?: "Not Found"

        // Check if stock is null or 0
        productQuantityTextView.text = "${product.stock ?: 0}X"

        // Handle image loading with Glide, add placeholder for null imageUrl
        if (!product.image_url.isNullOrEmpty()) {
            Glide.with(context)
                .load(product.image_url)
                .placeholder(android.R.drawable.ic_menu_gallery) // Placeholder image
                .into(productImageView)
        } else {
            // If no image URL, show placeholder image
            productImageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        return view
    }
}
