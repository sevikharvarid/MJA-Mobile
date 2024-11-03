package com.example.majujayaaccessories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
//import com.bumptech.glide.Glide


class ProductGridAdapter(private val context: Context, private val productList: List<Product>) : BaseAdapter() {

    override fun getCount(): Int = productList.size

    override fun getItem(position: Int): Any = productList[position]

    override fun getItemId(position: Int): Long = productList[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)

        val product = getItem(position) as Product
        val productImageView: ImageView = view.findViewById(R.id.productImageView)
        val productNameTextView: TextView = view.findViewById(R.id.productNameTextView)
        val productQuantityTextView: TextView = view.findViewById(R.id.productQuantityTextView)

        productNameTextView.text = product.productName
        productQuantityTextView.text = "Jumlah: ${product.stock}"

        // Load image if available
//        Glide.with(context).load(product.imageUrl).placeholder(android.R.drawable.ic_menu_gallery).into(productImageView)

        return view
    }
}
