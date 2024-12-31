package com.example.majujayaaccessories.admin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.majujayaaccessories.R
import com.example.majujayaaccessories.databinding.ItemAdminStoreBinding
import com.example.majujayaaccessories.databinding.ItemOrderBinding
import com.example.majujayaaccessories.databinding.ItemStockDetailBinding
import com.example.majujayaaccessories.response.ChartProduct
import com.example.majujayaaccessories.response.Order
import com.example.majujayaaccessories.response.Store

class AdminStoreAdapter : RecyclerView.Adapter<AdminStoreAdapter.ViewHolder>() {
    private var data = ArrayList<Store>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(store: List<Store>) {
        data.clear()
        data.addAll(store)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.view){
            Glide.with(holder.itemView.context)
                .load(data[position].user.image_url)
                .error(R.drawable.circle_background)
                .into(ivIconOrder)
            tvStoreTitle.text = data[position].id.toString()
            tvStoreName.text =  data[position].store_name
            tvStoreAddress.text =  data[position].store_address
            }
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemAdminStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    class ViewHolder(val view: ItemAdminStoreBinding) : RecyclerView.ViewHolder(view.root)
}
