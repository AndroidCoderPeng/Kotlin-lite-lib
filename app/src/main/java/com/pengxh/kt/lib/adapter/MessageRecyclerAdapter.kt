package com.pengxh.kt.lib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.model.MessageModel

class MessageRecyclerAdapter(
    private val context: Context,
    private val dataRows: MutableList<MessageModel>
) : RecyclerView.Adapter<MessageRecyclerAdapter.ItemViewHolder>() {

    private val kTag = "NormalRecyclerAdapter"

    override fun getItemCount(): Int = dataRows.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_message_tcp_rv_l, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model = dataRows[position]
        holder.timeView.text = model.time
        holder.messageView.text = model.message
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeView: TextView = itemView.findViewById(R.id.timeView)
        val messageView: TextView = itemView.findViewById(R.id.messageView)
    }
}