package com.example.majujayaaccessories.admin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.majujayaaccessories.R
import com.example.majujayaaccessories.admin.entity.StoreOrderData
import com.example.majujayaaccessories.databinding.ItemOrderBinding
import com.example.majujayaaccessories.response.ChartProduct
import com.example.majujayaaccessories.response.Order

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    private var data = ArrayList<StoreOrderData>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(order: List<StoreOrderData>) {
        data.clear()
        data.addAll(order)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.view){
            Glide.with(holder.itemView.context)
                .load(data[position].user.image_url)
                .error(R.drawable.circle_background)
                .into(ivIconOrder)
            tvOrder.text =  data[position].storeName
            tvOrderItem.text =  data[position].orderItems.joinToString { it.product.product_name }
            }
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    class ViewHolder(val view: ItemOrderBinding) : RecyclerView.ViewHolder(view.root)
}
