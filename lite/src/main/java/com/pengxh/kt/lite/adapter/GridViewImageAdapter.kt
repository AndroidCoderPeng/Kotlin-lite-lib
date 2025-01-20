package com.pengxh.kt.lite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.getScreenWidth

/**
 * 九宫格图片适配器，不可编辑
 * 仅支持 [android.widget.GridView]
 */
class GridViewImageAdapter(private val context: Context, private val images: List<String>) :
    BaseAdapter() {

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = images.size

    override fun getItem(position: Int): Any {
        if (position < 0 || position >= images.size) {
            throw IndexOutOfBoundsException("Position $position is out of bounds")
        }
        return images[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ItemViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_readonly_gv, null)
            holder = ItemViewHolder()
            holder.imageView = view.findViewById(R.id.imageView)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ItemViewHolder
        }
        Glide.with(context.applicationContext)
            .load(images[position])
            .apply(RequestOptions().error(R.mipmap.load_image_error))
            .into(holder.imageView)

        //动态设置图片高度，和图片宽度保持一致
        val screenWidth = context.getScreenWidth()
        val padding = (view.paddingLeft + view.paddingRight) * 3
        val imageSize = (screenWidth - padding) / 3
        if (holder.cachedLayoutParams == null) {
            holder.cachedLayoutParams = LinearLayout.LayoutParams(imageSize, imageSize)
        } else {
            holder.cachedLayoutParams?.width = imageSize
            holder.cachedLayoutParams?.height = imageSize
        }
        holder.imageView.layoutParams = holder.cachedLayoutParams
        return view
    }

    private class ItemViewHolder {
        lateinit var imageView: ImageView
        var cachedLayoutParams: LinearLayout.LayoutParams? = null
    }
}