package com.pengxh.kt.lite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pengxh.kt.lite.R
import java.util.*

/**
 * 不可编辑图片适配器
 */
class ReadOnlyImageAdapter(private val context: Context) : BaseAdapter() {

    private val images: MutableList<String> = ArrayList()

    fun setImageList(imageUrlList: MutableList<String>?) {
        images.clear()
        if (imageUrlList != null) {
            images.addAll(imageUrlList)
        }
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = images.size

    override fun getItem(position: Int): Any = images[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ItemViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_gridview_readonly, null)
            holder = ItemViewHolder()
            holder.imageView = view.findViewById(R.id.imageView)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ItemViewHolder
        }
        Glide.with(context)
            .load(images[position])
            .apply(RequestOptions().error(R.mipmap.load_image_error))
            .into(holder.imageView)
        return view
    }

    private class ItemViewHolder {
        lateinit var imageView: ImageView
    }
}