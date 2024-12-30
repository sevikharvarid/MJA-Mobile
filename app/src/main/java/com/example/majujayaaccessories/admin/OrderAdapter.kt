package com.example.majujayaaccessories.admin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.majujayaaccessories.R
import com.example.majujayaaccessories.databinding.ItemOrderBinding
import com.example.majujayaaccessories.response.ChartProduct
import com.example.majujayaaccessories.response.Order

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    private var data = ArrayList<Order>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(order: List<Order>) {
        data.clear()
        data.addAll(order)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.view){
            tvOrder.text =  data[position].store_id.toString()
            tvOrderItem.text =  data[position].order_items.joinToString { it.product.product_name }
            }
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    class ViewHolder(val view: ItemOrderBinding) : RecyclerView.ViewHolder(view.root)
}
