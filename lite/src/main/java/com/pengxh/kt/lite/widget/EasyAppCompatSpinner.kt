package com.pengxh.kt.lite.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

/**
 * 选同一位置依旧可以触发事件的Spinner
 * */
class EasyAppCompatSpinner constructor(context: Context, attrs: AttributeSet) :
    AppCompatSpinner(context, attrs) {

    override fun setSelection(position: Int, animate: Boolean) {
        val sameSelected = position == selectedItemPosition
        super.setSelection(position, animate)
        if (sameSelected) {
            onItemSelectedListener?.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }

    override fun setSelection(position: Int) {
        val sameSelected = position == selectedItemPosition
        super.setSelection(position)
        if (sameSelected) {
            onItemSelectedListener?.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }
}