package com.pengxh.kt.lite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView多选适配器
 */
abstract class MultipleChoiceAdapter<T>(
    @LayoutRes private val xmlResource: Int, private val dataRows: MutableList<T>
) : RecyclerView.Adapter<ViewHolder>() {

    private val kTag = "MultipleChoiceAdapter"
    private var multipleSelected = mutableSetOf<Int>()
    private var selectedItems = ArrayList<T>()

    override fun getItemCount(): Int = dataRows.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(xmlResource, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        convertView(holder, position, dataRows[position])

        holder.itemView.isSelected = multipleSelected.contains(position)
        holder.itemView.setOnClickListener {
            if (multipleSelected.contains(position)) {
                multipleSelected.remove(position)
                selectedItems.remove(dataRows[position])
                holder.itemView.isSelected = false
            } else {
                multipleSelected.add(position)
                selectedItems.add(dataRows[position])
                holder.itemView.isSelected = true
            }

            itemCheckedListener?.onItemChecked(position, selectedItems)
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

    private var itemCheckedListener: OnItemCheckedListener<T>? = null

    interface OnItemCheckedListener<T> {
        fun onItemChecked(position: Int, items: ArrayList<T>)
    }

    fun setOnItemCheckedListener(listener: OnItemCheckedListener<T>?) {
        itemCheckedListener = listener
    }
}