package com.pengxh.kt.lite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


/**
 * RecyclerView普通列表适配器
 */
abstract class NormalRecyclerAdapter<T>(
    @LayoutRes private val xmlResource: Int, private val dataRows: MutableList<T>
) : RecyclerView.Adapter<ViewHolder>() {

    private val kTag = "NormalRecyclerAdapter"

    override fun getItemCount(): Int = dataRows.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(xmlResource, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        convertView(holder, position, dataRows[position])
        holder.itemView.setOnClickListener {
            itemClickedListener?.onItemClicked(position, dataRows[position])
        }
    }

    /**
     * 刷新列表，局部刷新
     * */
    fun refresh(newRows: MutableList<T>, diffCallback: DiffUtil.Callback?) {
        if (newRows.isEmpty()) return
        if (diffCallback != null) {
            val diffResult = DiffUtil.calculateDiff(diffCallback)

            dataRows.clear()
            dataRows.addAll(newRows)

            diffResult.dispatchUpdatesTo(this)
        } else {
            dataRows.clear()
            dataRows.addAll(newRows)
            notifyItemRangeChanged(0, dataRows.size)
        }
    }

    /**
     * 加载更多
     * */
    fun loadMore(newRows: MutableList<T>) {
        if (newRows.isEmpty()) {
            return
        }
        val startPosition = this.dataRows.size
        val newSize = newRows.size
        this.dataRows.addAll(newRows)
        notifyItemRangeInserted(startPosition, newSize)
    }

    abstract fun convertView(viewHolder: ViewHolder, position: Int, item: T)

    private var itemClickedListener: OnItemClickedListener<T>? = null

    interface OnItemClickedListener<T> {
        fun onItemClicked(position: Int, item: T)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener<T>?) {
        itemClickedListener = listener
    }
}