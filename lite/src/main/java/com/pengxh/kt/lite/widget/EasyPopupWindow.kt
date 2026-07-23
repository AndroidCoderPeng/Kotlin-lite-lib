package com.pengxh.kt.lite.widget

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import com.pengxh.kt.lite.R

class EasyPopupWindow(context: Context, popupWidth: Int) : PopupWindow() {

    init {
        width = popupWidth
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        contentView = LayoutInflater.from(context).inflate(R.layout.popup_menu_option, null)
    }

    fun set(
        menuItems: List<MenuItem>,
        onPopupItemClicked: (popup: EasyPopupWindow, position: Int) -> Unit
    ) {
        if (menuItems.isEmpty()) {
            return
        }

        val listView = contentView.findViewById<ListView>(R.id.listView)
        val inflater = LayoutInflater.from(contentView.context)
        listView.adapter = object : BaseAdapter() {
            override fun getCount(): Int {
                return menuItems.size
            }

            override fun getItem(position: Int): Any {
                return menuItems[position]
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

                // 添加安全检查防止资源错误
                if (position >= 0 && position < menuItems.size) {
                    holder.imageView.setBackgroundResource(menuItems[position].icon)
                    holder.titleView.text = menuItems[position].name
                }
                return view
            }
        }
        listView.setOnItemClickListener { _, _, position, _ ->
            if (position >= 0 && position < menuItems.size) {
                onPopupItemClicked(this, position)
            }
        }
    }

    private class PopupWindowHolder {
        lateinit var imageView: ImageView
        lateinit var titleView: TextView
    }

    /**
     * 菜单数据模型
     * */
    data class MenuItem(val icon: Int, val name: String)
}