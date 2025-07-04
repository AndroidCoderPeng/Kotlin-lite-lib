package com.pengxh.kt.lite.widget

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.appcompat.widget.AppCompatEditText
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.convertDrawable


class DeleteEditText(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs), OnFocusChangeListener, TextWatcher {

    private val kTag = "DeleteEditText"
    private val paint = Paint()
    private val bounds = Rect()

    /**
     * 删除按钮的引用
     */
    private var clearDrawable = compoundDrawables[2]
    private var hasFocus = false

    init {
        /**
         * 获取EditText的DrawableRight
         * 2是获得右边的图片，顺序是左上右下（0,1,2,3,）
         * */
        if (clearDrawable == null) {
            clearDrawable = R.drawable.ic_edit_text_delete.convertDrawable(context)
        }

        paint.textSize = textSize
        paint.typeface = typeface

        setClearIconVisible(false)
        onFocusChangeListener = this
        addTextChangedListener(this)
        //获取输入框文字的高度
        post {
            //定义一个默认文字适配用户自定义的EditText的文字大小
            val tempText = "DeleteEditText"
            paint.getTextBounds(tempText, 0, tempText.length, bounds)

            //设置图标大小
            val iconSize = (bounds.height() * 1.5).toInt()
            Log.d(kTag, "EditeText View Text Height: ${bounds.height()}, Clear IconSize: $iconSize")
            clearDrawable.setBounds(0, 0, iconSize, iconSize)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.x > width - totalPaddingRight && event.x < width - paddingRight) {
                setText("")
            }
        }
        return super.onTouchEvent(event)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (hasFocus) {
            setClearIconVisible(s.isNotEmpty())
        }
    }

    override fun afterTextChanged(s: Editable) {}

    /**
     * 当EditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        if (hasFocus) {
            setClearIconVisible(text.toString().isNotEmpty())
        } else {
            setClearIconVisible(false)
        }
    }

    private fun setClearIconVisible(visible: Boolean) {
        val rightIcon = if (visible) {
            clearDrawable
        } else {
            null
        }
        setCompoundDrawables(
            compoundDrawables[0], compoundDrawables[1], rightIcon, compoundDrawables[3]
        )
    }
}