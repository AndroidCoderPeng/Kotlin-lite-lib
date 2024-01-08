package com.pengxh.kt.lite.utils

import android.app.Activity
import java.util.Stack

object ActivityStackManager {
    private val activityStack = Stack<Activity>()

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity?) {
        if (activity == null) {
            return
        }
        activityStack.push(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return activityStack.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishCurrentActivity() {
        val activity = activityStack.pop() ?: return
        activity.finish()
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity == null) {
            return
        }
        activityStack.remove(activity)
        if (!activity.isFinishing) {
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun <T> finishActivity(clazz: Class<T>) {
        for (activity in activityStack) {
            if (activity.javaClass == clazz) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        for (activity in activityStack) {
            activity.finish()
        }
        activityStack.clear()
    }
}