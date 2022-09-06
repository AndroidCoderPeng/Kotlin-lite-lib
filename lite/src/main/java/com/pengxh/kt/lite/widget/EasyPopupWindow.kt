package com.pengxh.kt.lite.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.obtainScreenWidth

class EasyPopupWindow constructor(context: Context) : PopupWindow() {
    private var clickListener: OnPopupWindowClickListener? = null

    init {
        width = ((context.obtainScreenWidth() * 0.3).toInt())
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isOutsideTouchable = true
        isFocusable = true
        animationStyle = R.style.PopupAnimation
        setBackgroundDrawable(null)
        contentView = LayoutInflater.from(context).inflate(
            R.layout.popup_menu_option, null, false
        )
    }

    fun setPopupMenuItem(imageArray: IntArray, titleArray: Array<String>) {
        try {
            val popupListView = contentView.findViewById<ListView>(R.id.popupListView)
            popupListView.adapter = object : BaseAdapter() {
                private val inflater: LayoutInflater = LayoutInflater.from(contentView.context)
                override fun getCount(): Int {
                    return imageArray.size
                }

                override fun getItem(position: Int): Any {
                    return imageArray[position]
                }

                override fun getItemId(position: Int): Long {
                    return position.toLong()
                }

                override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                    val view: View
                    val holder: PopupWindowHolder
                    if (convertView == null) {
                        view = inflater.inflate(R.layout.item_popup_menu, null)
                        holder = PopupWindowHolder()
                        holder.imageView = view.findViewById(R.id.imageView)
                        holder.titleView = view.findViewById(R.id.titleView)
                        view.tag = holder
                    } else {
                        view = convertView
                        holder = view.tag as PopupWindowHolder
                    }
                    holder.imageView.setBackgroundResource(imageArray[position])
                    holder.titleView.text = titleArray[position]
                    return view
                }
            }
            popupListView.setOnItemClickListener { _, _, position, _ ->
                clickListener!!.onPopupItemClicked(position)
                dismiss()
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    interface OnPopupWindowClickListener {
        fun onPopupItemClicked(position: Int)
    }

    fun setOnPopupWindowClickListener(windowClickListener: OnPopupWindowClickListener?) {
        clickListener = windowClickListener
    }

    internal class PopupWindowHolder {
        lateinit var imageView: ImageView
        lateinit var titleView: TextView
    }
}