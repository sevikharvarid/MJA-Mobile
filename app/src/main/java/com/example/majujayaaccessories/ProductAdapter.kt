package com.example.majujayaaccessories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

data class ProductOrder(
    val id: Int,
    val productName: String,
    val productDescription: String,
    val price: Int,
    var stock: Int,
    val imageUrl: String
)

class ProductAdapter(
    private val context: Context,
    private val products: List<ProductOrder>,
    private val onStockChange: (ProductOrder) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = products.size

    override fun getItem(position: Int): Any = products[position]

    override fun getItemId(position: Int): Long = products[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_order_product, parent, false)
        val product = products[position]

        val imgProduct: ImageView = view.findViewById(R.id.imgProduct)
        val txtName: TextView = view.findViewById(R.id.txtName)
        val txtStock: TextView = view.findViewById(R.id.txtStock)
        val btnIncrease: Button = view.findViewById(R.id.btnIncrease)
        val btnDecrease: Button = view.findViewById(R.id.btnDecrease)

        txtName.text = product.productName
        txtStock.text = "Stok: ${product.stock}"
        Glide.with(context).load(product.imageUrl).into(imgProduct)

        btnIncrease.setOnClickListener {
            product.stock++
            txtStock.text = "Stok: ${product.stock}"
            onStockChange(product)
        }

        btnDecrease.setOnClickListener {
            if (product.stock > 0) {
                product.stock--
                txtStock.text = "Stok: ${product.stock}"
                onStockChange(product)
            } else {
                Toast.makeText(context, "Stok tidak boleh kurang dari 0", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}

