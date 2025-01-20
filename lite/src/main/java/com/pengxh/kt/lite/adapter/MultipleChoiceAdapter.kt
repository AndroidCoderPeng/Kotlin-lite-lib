package com.pengxh.kt.lite.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * RecyclerView多选适配器
 */
abstract class MultipleChoiceAdapter<T>(
    @LayoutRes private val xmlResource: Int, private val dataRows: MutableList<T>
) : RecyclerView.Adapter<ViewHolder>() {

    private val kTag = "MultipleChoiceAdapter"
    private var multipleSelected = ConcurrentHashMap<Int, Boolean>()
    private var selectedItems = CopyOnWriteArrayList<T>()

    override fun getItemCount(): Int = dataRows.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(xmlResource, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < 0 || position >= dataRows.size) {
            Log.d(kTag, "Invalid position: $position")
            return
        }

        convertView(holder, position, dataRows[position])

        holder.itemView.isSelected = multipleSelected.containsKey(position)
        holder.itemView.setOnClickListener {
            if (multipleSelected.containsKey(position)) {
                multipleSelected.remove(position)
                selectedItems.removeIf { it == dataRows[position] }
                holder.itemView.isSelected = false
            } else {
                multipleSelected[position] = true
                selectedItems.add(dataRows[position])
                holder.itemView.isSelected = true
            }

            itemCheckedListener?.onItemChecked(selectedItems) ?: run {
                Log.d(kTag, "No listener set for item checked events")
            }
        }
    }

    /**
     * 加载更多
     * */
    fun loadMore(newRows: MutableList<T>) {
        val startPosition = dataRows.size
        this.dataRows.addAll(newRows)
        notifyItemRangeInserted(startPosition, newRows.size)
    }

    abstract fun convertView(viewHolder: ViewHolder, position: Int, item: T)

    private var itemCheckedListener: OnItemCheckedListener<T>? = null

    interface OnItemCheckedListener<T> {
        fun onItemChecked(items: List<T>)
    }

    fun setOnItemCheckedListener(listener: OnItemCheckedListener<T>?) {
        itemCheckedListener = listener
    }
}