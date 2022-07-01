package com.pengxh.kt.lite.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
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
 * */
@SuppressLint("NotifyDataSetChanged")
class EditableImageAdapter(private val context: Context, private val imageCountLimit: Int) :
    RecyclerView.Adapter<EditableImageAdapter.ItemViewHolder?>() {

    private var imageData: MutableList<String> = ArrayList()
    private var leftMargin = 0f
    private var rightMargin = 0f
    private var padding = 3f

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

    /**
     * @param leftMargin RecyclerView左边距
     * @param rightMargin RecyclerView右边距
     * @param padding RecyclerView内边距
     * */
    fun setImageMargins(leftMargin: Float, rightMargin: Float, padding: Float) {
        this.leftMargin = leftMargin
        this.rightMargin = rightMargin
        this.padding = padding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val imageView = ImageView(context)
        val realWidth: Int =
            context.obtainScreenWidth() - (leftMargin.dp2px(context) + rightMargin.dp2px(context))
        val padding: Int = padding.dp2px(context)
        val itemSize = (realWidth - 4 * padding) / imageCountLimit
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(itemSize, itemSize)
        params.setMargins(padding, padding, padding, padding)
        params.gravity = Gravity.CENTER
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = params
        return ItemViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (position == itemCount - 1 && imageData.size < imageCountLimit) {
            holder.imageView.setImageResource(R.drawable.ic_add_pic)
            holder.imageView.setOnClickListener { //添加图片
                mOnItemClickListener!!.onAddImageClick()
            }
        } else {
            Glide.with(context).load(imageData[position]).into(holder.imageView)
            holder.imageView.setOnClickListener { // 点击操作，查看大图
                mOnItemClickListener!!.onItemClick(position)
            }
            // 长按监听
            holder.imageView.setOnLongClickListener { v -> //长按删除
                mOnItemClickListener!!.onItemLongClick(v, position)
                true
            }
        }
    }

    override fun getItemCount(): Int =
        if (imageData.size >= imageCountLimit) {
            imageCountLimit
        } else {
            imageData.size + 1
        }

    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onAddImageClick()
        fun onItemClick(position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView as ImageView
    }
}