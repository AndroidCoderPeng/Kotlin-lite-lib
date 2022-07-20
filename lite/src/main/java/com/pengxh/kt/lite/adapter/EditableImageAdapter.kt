package com.pengxh.kt.lite.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.extensions.obtainScreenWidth


/**
 * 数量可编辑图片适配器
 *
 * @param imageCountLimit 最多显示几张图片
 * @param spacing RecyclerView边距，左右外边距+ImageView内边距,单位dp
 * */
@SuppressLint("NotifyDataSetChanged")
class EditableImageAdapter(
    private val context: Context, private val imageCountLimit: Int, private val spacing: Float
) : RecyclerView.Adapter<EditableImageAdapter.ItemViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val screenWidth = context.obtainScreenWidth()
    private var imageData: MutableList<String> = ArrayList()

    fun setupImage(images: MutableList<String>) {
        imageData = images
        notifyDataSetChanged()
    }

    fun deleteImage(position: Int) {
        if (imageData.isNotEmpty()) {
            imageData.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            layoutInflater.inflate(R.layout.item_gridview_editable, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        configImageParams(holder.imageView, position)
        if (position == itemCount - 1 && imageData.size < imageCountLimit) {
            holder.imageView.setImageResource(R.drawable.ic_add_pic)
            holder.imageView.setOnClickListener { //添加图片
                itemClickListener?.onAddImageClick()
            }
        } else {
            Glide.with(context).load(imageData[position]).into(holder.imageView)
            holder.imageView.setOnClickListener { // 点击操作，查看大图
                itemClickListener?.onItemClick(position)
            }
            // 长按监听
            holder.imageView.setOnLongClickListener { v -> //长按删除
                itemClickListener?.onItemLongClick(v, position)
                true
            }
        }
    }

    private fun configImageParams(imageView: ImageView, position: Int) {
        val totalPadding = spacing.dp2px(context) * 4
        val imageSize = (screenWidth - totalPadding) / 3

        val params = LinearLayout.LayoutParams(imageSize, imageSize)
        when (position) {
            in 3..5 -> {
                params.setMargins(0, spacing.dp2px(context), 0, spacing.dp2px(context))
            }
            else -> {
                params.setMargins(0, 0, 0, 0)
            }
        }
        imageView.layoutParams = params
    }

    override fun getItemCount(): Int = if (imageData.size >= imageCountLimit) {
        imageCountLimit
    } else {
        imageData.size + 1
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

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}