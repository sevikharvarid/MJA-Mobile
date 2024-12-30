package com.example.majujayaaccessories.admin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.majujayaaccessories.R
import com.example.majujayaaccessories.admin.entity.ChartDataSet
import com.example.majujayaaccessories.databinding.ItemOrderBinding
import com.example.majujayaaccessories.databinding.ItemStockBinding
import com.example.majujayaaccessories.response.ChartProduct
import com.example.majujayaaccessories.response.Order

class LegendStockAdapter : RecyclerView.Adapter<LegendStockAdapter.ViewHolder>() {
    private var data = ArrayList<ChartDataSet>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(chartProduct: List<ChartDataSet>) {
        data.clear()
        data.addAll(chartProduct)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.view){
            tvLabel.text =  data[position].label
            tvValue.text =  "(${Math.round(data[position].value).toInt()}%)"
            vwLegendColor.setBackgroundColor(data[position].color)
            }
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    class ViewHolder(val view: ItemStockBinding) : RecyclerView.ViewHolder(view.root)
}
