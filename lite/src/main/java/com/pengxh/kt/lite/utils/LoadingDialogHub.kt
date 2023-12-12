package com.pengxh.kt.lite.utils

import android.app.Activity
import android.app.ProgressDialog
import android.view.WindowManager

object LoadingDialogHub {
    private lateinit var loadingDialog: ProgressDialog

    fun show(activity: Activity, message: String) {
        if (!activity.isDestroyed) {
            try {
                loadingDialog = ProgressDialog.show(activity, "", message)
            } catch (e: WindowManager.BadTokenException) {
                e.printStackTrace()
            }
        }
    }

    fun show(activity: Activity, title: String, message: String) {
        if (!activity.isDestroyed) {
            try {
                loadingDialog = ProgressDialog.show(activity, title, message)
            } catch (e: WindowManager.BadTokenException) {
                e.printStackTrace()
            }
        }
    }

    fun dismiss() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }
}