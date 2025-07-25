package com.pengxh.kt.lite.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    fun refresh(newRows: MutableList<T>, itemComparator: ItemComparator<T>? = null) {
        if (newRows.isEmpty()) {
            Log.d(kTag, "refresh: newRows isEmpty")
            return
        }

        val oldSize = dataRows.size

        if (itemComparator != null) {
            val oldDataSnapshot = ArrayList(dataRows) // 旧数据副本
            val newDataSnapshot = ArrayList(newRows)  // 新数据副本

            val diffCallback = object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = oldDataSnapshot.size
                override fun getNewListSize(): Int = newDataSnapshot.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return itemComparator.areItemsTheSame(
                        oldDataSnapshot[oldItemPosition], newDataSnapshot[newItemPosition]
                    )
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int, newItemPosition: Int
                ): Boolean {
                    return itemComparator.areContentsTheSame(
                        oldDataSnapshot[oldItemPosition], newDataSnapshot[newItemPosition]
                    )
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = DiffUtil.calculateDiff(diffCallback)
                    withContext(Dispatchers.Main) {
                        dataRows.clear()
                        dataRows.addAll(newDataSnapshot)
                        result.dispatchUpdatesTo(this@NormalRecyclerAdapter)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            val newSize = newRows.size
            dataRows.clear()
            dataRows.addAll(newRows)

            // 新数据比旧数据少，需要通知删除部分 item ，否则会越界
            if (newSize < oldSize) {
                notifyItemRangeRemoved(newSize, oldSize - newSize)
            }
            notifyItemRangeChanged(0, newSize)
        }
    }

    /**
     * 加载更多
     * */
    fun loadMore(newRows: MutableList<T>) {
        if (newRows.isEmpty()) {
            Log.d(kTag, "loadMore: newRows isEmpty")
            return
        }
        val startPosition = dataRows.size
        val newSize = newRows.size
        dataRows.addAll(newRows)
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

    interface ItemComparator<T> {
        fun areItemsTheSame(oldItem: T, newItem: T): Boolean

        fun areContentsTheSame(oldItem: T, newItem: T): Boolean
    }
}