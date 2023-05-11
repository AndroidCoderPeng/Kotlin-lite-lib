package com.pengxh.kt.lite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView


/**
 * RecyclerView普通列表适配器
 */
abstract class NormalRecyclerAdapter<T>(
    @LayoutRes private val xmlResource: Int, private val dataRows: MutableList<T>
) : RecyclerView.Adapter<ViewHolder>() {

    private val kTag = "NormalRecyclerAdapter"

    override fun getItemCount(): Int = dataRows.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(xmlResource, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        convertView(holder, position, dataRows[position])
        holder.itemView.setOnClickListener {
            itemClickedListener?.onItemClicked(position, dataRows[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRefreshData(dataRows: MutableList<T>) {
        this.dataRows.clear()
        this.dataRows.addAll(dataRows)
        notifyDataSetChanged()
    }

    fun setLoadMoreData(dataRows: MutableList<T>) {
        this.dataRows.addAll(dataRows)
        notifyItemRangeInserted(this.dataRows.size, dataRows.size)
    }

    abstract fun convertView(viewHolder: ViewHolder, position: Int, item: T)

    private var itemClickedListener: OnItemClickedListener<T>? = null

    interface OnItemClickedListener<T> {
        fun onItemClicked(position: Int, t: T)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener<T>?) {
        itemClickedListener = listener
    }
}