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
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.extensions.getScreenWidth


/**
 * 数量可编辑图片适配器
 *
 * @param imageCountLimit 最多显示几张图片，每行3张图片
 * @param spacing 上下左右外边距，无需在 [androidx.recyclerview.widget.RecyclerView] 设置边距
 * */
class EditableImageAdapter(
    private val context: Context, private val imageCountLimit: Int, private val spacing: Float
) : RecyclerView.Adapter<ViewHolder>() {

    private val screenWidth by lazy { context.getScreenWidth() }
    private var images: MutableList<String> = ArrayList()

    fun setupImage(images: MutableList<String>) {
        this.images = images
        notifyItemRangeChanged(0, images.size)
    }

    fun deleteImage(position: Int) {
        if (images.isNotEmpty()) {
            images.removeAt(position)
            /**
             * 发生变化的item数目
             * */
            notifyItemRangeRemoved(position, 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_editable_rv_g, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageView = holder.getView<ImageView>(R.id.imageView)
        configImageParams(imageView, holder.bindingAdapterPosition)
        if (position == itemCount - 1 && images.size < imageCountLimit) {
            imageView.setImageResource(R.drawable.ic_add_pic)
            imageView.setOnClickListener { //添加图片
                itemClickListener?.onAddImageClick()
            }
        } else {
            Glide.with(context).load(images[position]).into(imageView)
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

    private fun configImageParams(imageView: ImageView, position: Int) {
        val temp = spacing.dp2px(context)
        val imageSize = (screenWidth - temp * 3) / 3

        val params = LinearLayout.LayoutParams(imageSize, imageSize)
        when (position) {
            0 -> params.setMargins(temp, temp, temp shr 1, temp shr 1)
            1 -> params.setMargins(temp shr 1, temp, temp shr 1, temp shr 1)
            2 -> params.setMargins(temp shr 1, temp, temp, temp shr 1)
            3 -> params.setMargins(temp, temp shr 1, temp shr 1, temp shr 1)
            4 -> params.setMargins(temp shr 1, temp shr 1, temp shr 1, temp shr 1)
            5 -> params.setMargins(temp shr 1, temp shr 1, temp, temp shr 1)
            6 -> params.setMargins(temp, temp shr 1, temp shr 1, temp)
            7 -> params.setMargins(temp shr 1, temp shr 1, temp shr 1, temp)
            8 -> params.setMargins(temp shr 1, temp shr 1, temp, temp)
        }
        imageView.layoutParams = params
    }

    override fun getItemCount(): Int = if (images.size >= imageCountLimit) {
        imageCountLimit
    } else {
        images.size + 1
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