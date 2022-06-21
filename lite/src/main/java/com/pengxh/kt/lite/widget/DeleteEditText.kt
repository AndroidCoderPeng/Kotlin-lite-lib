package com.pengxh.kt.lite.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.TranslateAnimation
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.pengxh.kt.lite.R

class DeleteEditText constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr), OnFocusChangeListener, TextWatcher {

    /**
     * 删除按钮的引用
     */
    private var rightClearDrawable: Drawable?
    private var hasFocus = false

    init {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,2是获得右边的图片  顺序是左上右下（0,1,2,3,）
        rightClearDrawable = compoundDrawables[2]
        if (rightClearDrawable == null) {
            rightClearDrawable =
                ResourcesCompat.getDrawable(context.resources, R.drawable.ic_edit_text_delete, null)
        }
        //设置图标大小
        rightClearDrawable!!.setBounds(0, 0, 64, 64)
        setClearIconVisible(false)
        onFocusChangeListener = this
        addTextChangedListener(this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (compoundDrawables[2] != null) {
                val touchable =
                    event.x > width - totalPaddingRight && event.x < width - paddingRight
                if (touchable) {
                    this.setText("")
                }
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
     * 设置晃动动画
     */
    fun setShakeAnimation() {
        val animation = TranslateAnimation(0f, 10f, 0f, 0f)
        animation.interpolator = OvershootInterpolator()
        animation.duration = 30
        animation.repeatCount = 5
        animation.repeatMode = Animation.REVERSE
        startAnimation(animation)
    }

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
        val right = if (visible) rightClearDrawable else null
        setCompoundDrawables(
            compoundDrawables[0], compoundDrawables[1], right, compoundDrawables[3]
        )
    }
}