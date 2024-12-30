package com.example.majujayaaccessories.admin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.majujayaaccessories.R
import com.example.majujayaaccessories.databinding.ItemOrderBinding
import com.example.majujayaaccessories.databinding.ItemStockDetailBinding
import com.example.majujayaaccessories.response.ChartProduct
import com.example.majujayaaccessories.response.Order

class StockDetailAdapter : RecyclerView.Adapter<StockDetailAdapter.ViewHolder>() {
    private var data = ArrayList<ChartProduct>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(chartProduct: List<ChartProduct>) {
        data.clear()
        data.addAll(chartProduct)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.view){
            tvTitle.text = data[position].product_name
            tvValue.text =  "${data[position].current_stock}/${data[position].total_stock}"
            }
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemStockDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    class ViewHolder(val view: ItemStockDetailBinding) : RecyclerView.ViewHolder(view.root)
}
