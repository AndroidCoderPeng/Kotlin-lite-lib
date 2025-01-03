package com.pengxh.kt.lite.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView


/**
 * RecyclerView单选适配器
 */
abstract class SingleChoiceAdapter<T>(
    @LayoutRes private val xmlResource: Int, private val dataRows: MutableList<T>
) : RecyclerView.Adapter<ViewHolder>() {

    private val kTag = "SingleChoiceAdapter"

    //选择的位置
    private var selectedPosition = -1

    fun setSelectedPosition(position: Int) {
        if (position in 0 until dataRows.size) {
            selectedPosition = position
        } else {
            Log.d(kTag, "Invalid position: $position")
        }
    }

    override fun getItemCount(): Int = dataRows.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(xmlResource, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position in 0 until dataRows.size) {
            convertView(holder, position, dataRows[position])

            holder.itemView.isSelected = holder.layoutPosition == selectedPosition
            holder.itemView.setOnClickListener {
                if (holder.layoutPosition != selectedPosition) {
                    val oldPosition = selectedPosition
                    selectedPosition = holder.layoutPosition
                    holder.itemView.isSelected = true
                    notifyItemChanged(oldPosition)
                    itemCheckedListener?.onItemChecked(selectedPosition, dataRows[selectedPosition])
                }
            }
        } else {
            Log.d(kTag, "Invalid position: $position")
        }
    }

    /**
     * 加载更多，局部加载
     * */
    fun loadMore(newRows: MutableList<T>) {
        val startPosition = dataRows.size
        dataRows.addAll(newRows)
        notifyItemRangeInserted(startPosition, newRows.size)
    }

    abstract fun convertView(viewHolder: ViewHolder, position: Int, item: T)

    private var itemCheckedListener: OnItemCheckedListener<T>? = null

    interface OnItemCheckedListener<T> {
        fun onItemChecked(position: Int, item: T)
    }

    fun setOnItemCheckedListener(listener: OnItemCheckedListener<T>?) {
        itemCheckedListener = listener
    }
}