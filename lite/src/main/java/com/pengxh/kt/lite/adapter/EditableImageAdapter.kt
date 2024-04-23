package com.pengxh.kt.lite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pengxh.kt.lite.R


/**
 * 数量可编辑图片适配器
 *
 * @param context 使用适配的上下文
 * @param viewWidth RecyclerView实际宽度，一般情况下就是屏幕宽度，但是如果有其他控件和它在同一行，需要计算实际宽度，不然无法正确显示RecyclerView item的布局
 * @param imageCountLimit 最多显示的图片数目
 * @param spanCount 每行显示的图片数目
 * */
class EditableImageAdapter(
    private val context: Context,
    private val viewWidth: Int,
    private val imageCountLimit: Int,
    private val spanCount: Int
) : RecyclerView.Adapter<ViewHolder>() {

    private var adapterItems = ArrayList<String>()

    fun notifyImageItemRangeInserted(images: ArrayList<String>) {
        val previousSize = adapterItems.size
        adapterItems.clear()
        notifyItemRangeRemoved(0, previousSize)
        adapterItems.addAll(images)
        notifyItemRangeInserted(0, adapterItems.size)
    }

    fun notifyImageItemRemoved(images: ArrayList<String>) {
        if (adapterItems.isNotEmpty()) {
            notifyImageItemRangeInserted(images)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_editable_rv_g, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageView = holder.getView<ImageView>(R.id.imageView)
        val imageSize = viewWidth / spanCount
        val params = LinearLayout.LayoutParams(imageSize, imageSize)
        imageView.layoutParams = params

        if (position == itemCount - 1 && adapterItems.size < imageCountLimit) {
            imageView.setImageResource(R.drawable.ic_add_pic)
            imageView.setOnClickListener { //添加图片
                itemClickListener?.onAddImageClick()
            }
        } else {
            Glide.with(context).load(adapterItems[position]).into(imageView)
            imageView.setOnClickListener { // 点击操作，查看大图
                itemClickListener?.onItemClick(holder.bindingAdapterPosition)
            }
            // 长按监听
            imageView.setOnLongClickListener { v -> //长按删除
                itemClickListener?.onItemLongClick(v, holder.bindingAdapterPosition)
                true
            }
        }
    }

    override fun getItemCount(): Int = if (adapterItems.size >= imageCountLimit) {
        imageCountLimit
    } else {
        adapterItems.size + 1
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun onAddImageClick()

        fun onItemClick(position: Int)

        fun onItemLongClick(view: View?, position: Int)
    }
}