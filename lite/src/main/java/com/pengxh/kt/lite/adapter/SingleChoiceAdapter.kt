package com.pengxh.kt.lite.adapter

import android.annotation.SuppressLint
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
        selectedPosition = position
    }

    //临时记录上次选择的位置
    private var temp = -1

    override fun getItemCount(): Int = dataRows.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(xmlResource, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        convertView(holder, position, dataRows[position])

        holder.itemView.isSelected = holder.layoutPosition == selectedPosition
        holder.itemView.setOnClickListener {
            holder.itemView.isSelected = true
            temp = selectedPosition
            //设置新的位置
            selectedPosition = holder.layoutPosition
            //更新旧位置
            notifyItemChanged(temp)

            itemCheckedListener?.onItemChecked(position, dataRows[position])
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
        fun onItemChecked(position: Int, t: T)
    }

    fun setOnItemCheckedListener(listener: OnItemCheckedListener<T>?) {
        itemCheckedListener = listener
    }
}