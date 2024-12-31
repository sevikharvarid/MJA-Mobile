package com.example.majujayaaccessories.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majujayaaccessories.R
import com.example.majujayaaccessories.admin.entity.StoreOrder

class OrderAdminAdapter(
    private val context: Context,
    private var orderData: List<StoreOrder>,
    private val isFromOrder: Boolean = false
) : RecyclerView.Adapter<OrderAdminAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.tvCreatedAt)
        val productsRecyclerView: RecyclerView = itemView.findViewById(R.id.horizontalRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_history_admin_order, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = orderData[position]

        // Format tanggal
        holder.dateTextView.text = if (isFromOrder) "Hari Ini" else formatDate(history.createdAt)

        // Set adapter untuk daftar produk dalam pesanan
        val itemAdapter = OrderDetailAdminAdapter(context, history.storeOrder)
        holder.productsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = itemAdapter
        }
    }

    override fun getItemCount(): Int = orderData.size

    // Fungsi untuk memperbarui data dalam adapter
    fun updateList(newOrderHistories: List<StoreOrder>) {
        orderData = newOrderHistories.sortedByDescending { it.createdAt }
        notifyDataSetChanged()
    }

    // Fungsi untuk format tanggal menjadi "dd MMMM yyyy"
    private fun formatDate(dateString: String): String {
        // Contoh input: "2024-11-14"
        val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val outputFormat = java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale.getDefault())
        return try {
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: dateString)
        } catch (e: Exception) {
            dateString // jika parsing gagal, gunakan nilai asli
        }
    }
}
