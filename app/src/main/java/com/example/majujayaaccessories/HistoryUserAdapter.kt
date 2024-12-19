package com.example.majujayaaccessories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majujayaaccessories.response.OrderHistory

class HistoryUserAdapter(
    private val context: Context,
    private var orderHistories: List<OrderHistory>
) : RecyclerView.Adapter<HistoryUserAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.tvCreatedAt)
        val productsRecyclerView: RecyclerView = itemView.findViewById(R.id.horizontalRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_history_order, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = orderHistories[position]

        // Format tanggal
        holder.dateTextView.text = formatDate(history.createdAt)

        // Set adapter untuk daftar produk dalam pesanan
        val itemAdapter = HistoryItemAdapter(context, history.orderItems)
        holder.productsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = itemAdapter
        }
    }

    override fun getItemCount(): Int = orderHistories.size

    // Fungsi untuk memperbarui data dalam adapter
    fun updateList(newOrderHistories: List<OrderHistory>) {
        orderHistories = newOrderHistories.sortedByDescending { it.createdAt }
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
