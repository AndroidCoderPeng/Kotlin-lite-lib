package com.pengxh.kt.lib.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.pengxh.kt.lib.R
import com.pengxh.kt.lite.extensions.convertColor

class SlideAdapter(private val context: Context, private val items: Array<String>) : BaseAdapter() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)
    private var selectedPosition = -1

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        val holder: SlideItemViewHolder?
        if (view == null) {
            view = inflater.inflate(R.layout.item_slide_list, parent, false)
            holder = SlideItemViewHolder()
            holder.textView = view.findViewById(R.id.textView) as TextView
            view.tag = holder
        } else {
            holder = view.tag as SlideItemViewHolder
        }
        if (selectedPosition == position) {
            holder.textView.setTextColor(R.color.mainColor.convertColor(context))
            holder.textView.typeface = Typeface.DEFAULT_BOLD
        } else {
            holder.textView.setTextColor(R.color.black.convertColor(context))
            holder.textView.typeface = Typeface.DEFAULT
        }
        holder.textView.text = items[position]
        return view
    }

    fun setSelectItem(position: Int) {
        selectedPosition = position
    }

    inner class SlideItemViewHolder {
        lateinit var textView: TextView
    }
}