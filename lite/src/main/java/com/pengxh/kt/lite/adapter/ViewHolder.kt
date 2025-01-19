package com.pengxh.kt.lite.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * 通用的 ViewHolder
 * */
class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val convertView: View = itemView
    private val views: SparseArray<View> = SparseArray()

    companion object {
        /**
         * @param view view
         * @return holder
         */
        fun create(view: View): ViewHolder {
            return ViewHolder(view)
        }
    }

    /**
     * 根据资源获取View对象
     *
     * @param res 控件ID
     * @param <T> 类型
     * @return 控件
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(@IdRes res: Int): T {
        var view = views[res]
        if (view == null) {
            view = convertView.findViewById(res)
            views.put(res, view)
        }
        return view as T
    }

    /**
     * 提供TextView和Button设置文本简化操作
     *
     * @param idRes        控件ID
     * @param charSequence 字符串
     * @return holder
     */
    fun setText(@IdRes idRes: Int, charSequence: CharSequence?): ViewHolder {
        val view = getView<View>(idRes)
        if (view is TextView) {
            view.text = charSequence
        } else if (view is Button) {
            view.text = charSequence
        }
        return this
    }

    /**
     * 提供TextView和Button设置文本颜色简化操作
     *
     * @param idRes 控件ID
     * @param color 颜色
     * @return holder
     */
    fun setTextColor(@IdRes idRes: Int, color: Int): ViewHolder {
        val view = getView<View>(idRes)
        if (view is TextView) {
            view.setTextColor(color)
        } else if (view is Button) {
            view.setTextColor(color)
        }
        return this
    }

    /**
     * 设置指定ViewId的背景颜色
     *
     * @param idRes 控件ID
     * @param color 颜色
     * @return holder
     */
    fun setBackgroundColor(@IdRes idRes: Int, color: Int): ViewHolder {
        val view = getView<View>(idRes)
        view.setBackgroundColor(color)
        return this
    }

    /**
     * 设置指定ViewId的可见度
     *
     * @param idRes      控件ID
     * @param visibility 可见度
     * @return holder
     */
    fun setVisibility(@IdRes idRes: Int, @DrawableRes visibility: Int): ViewHolder {
        val view = getView<View>(idRes)
        view.visibility = visibility
        return this
    }

    /**
     * 设置ImageView显示图片
     *
     * @param idRes 控件ID
     * @param resource   图片路径
     * @return holder
     */
    fun setImageResource(@IdRes idRes: Int, @DrawableRes resource: Int): ViewHolder {
        setImageInternal(idRes) { it.setImageResource(resource) }
        return this
    }

    /**
     * 设置ImageView显示图片
     *
     * @param idRes 控件ID
     * @param bitmap   图片Bitmap
     * @return holder
     */
    fun setImageResource(@IdRes idRes: Int, bitmap: Bitmap): ViewHolder {
        setImageInternal(idRes) { it.setImageBitmap(bitmap) }
        return this
    }

    /**
     * 设置ImageView显示图片
     *
     * @param idRes 控件ID
     * @param drawable   图片Drawable
     * @return holder
     */
    fun setImageResource(@IdRes idRes: Int, drawable: Drawable): ViewHolder {
        setImageInternal(idRes) { it.setImageDrawable(drawable) }
        return this
    }

    /**
     * 设置ImageView显示图片
     *
     * @param idRes    控件ID
     * @param imageUrl 图片网络地址
     * @return holder
     */
    fun setImageResource(@IdRes idRes: Int, imageUrl: String): ViewHolder {
        setImageInternal(idRes) { Glide.with(convertView).load(imageUrl).into(it) }
        return this
    }

    private fun setImageInternal(@IdRes idRes: Int, action: (ImageView) -> Unit): Boolean {
        val imageView = getView<ImageView>(idRes)
        action(imageView)
        return true
    }

    /**
     * 设置指定控件ID的点击事件
     *
     * @param idRes    控件ID
     * @param listener 监听接口
     * @return holder
     */
    fun setOnClickListener(@IdRes idRes: Int, listener: View.OnClickListener?): ViewHolder {
        val view = getView<View>(idRes)
        view.setOnClickListener(listener)
        return this
    }

    /**
     * 设置指定控件ID的长按事件
     *
     * @param idRes    控件ID
     * @param listener 监听接口
     * @return holder
     */
    fun setOnLongClickListener(@IdRes idRes: Int, listener: View.OnLongClickListener?): ViewHolder {
        val view = getView<View>(idRes)
        view.setOnLongClickListener(listener)
        return this
    }

    /**
     * 设置指定控件的TAG
     *
     * @param idRes 控件ID
     * @param tag   tag
     * @return holder
     */
    fun setTag(@IdRes idRes: Int, tag: Any?): ViewHolder {
        val view = getView<View>(idRes)
        view.tag = tag
        return this
    }

    /**
     * 获取指定控件的TAG
     *
     * @param idRes 控件ID
     * @return holder
     */
    fun getTag(@IdRes idRes: Int): Any {
        val view = getView<View>(idRes)
        return view.tag
    }
}