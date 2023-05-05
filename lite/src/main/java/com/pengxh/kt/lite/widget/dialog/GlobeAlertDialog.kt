package com.pengxh.kt.lite.widget.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.getScreenWidth


class GlobeAlertDialog(private val listener: OnDialogButtonClickListener) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val window = dialog?.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
        val params = window?.attributes
        params?.width = ((context?.getScreenWidth()!! * 0.8f).toInt())
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = params

        val view = inflater.inflate(R.layout.dialog_globe_alert, container, false)

        view.findViewById<Button>(R.id.dialogCancelButton).setOnClickListener {
            listener.onCancelClick()
        }

        view.findViewById<Button>(R.id.dialogConfirmButton).setOnClickListener {
            listener.onConfirmClick()
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    interface OnDialogButtonClickListener {
        fun onConfirmClick()

        fun onCancelClick()
    }
}