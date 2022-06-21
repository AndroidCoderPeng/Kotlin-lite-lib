package com.pengxh.kt.lite.utils

import android.content.Context
import android.content.SharedPreferences

object SaveKeyValues {
    private var sharedPreferences: SharedPreferences? = null

    fun initSharedPreferences(context: Context) {
        val packageName = context.packageName
        //获取到的包名带有“.”方便命名，取最后一个作为sp文件名
        val split = packageName.split("\\.").toTypedArray()
        val fileName = split[split.size - 1]
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    /**
     * 存储
     */
    fun putValue(key: String?, any: Any) {
        try {
            val editor = sharedPreferences!!.edit()
            when (any) {
                is String -> {
                    editor.putString(key, any)
                }
                is Int -> {
                    editor.putInt(key, any)
                }
                is Boolean -> {
                    editor.putBoolean(key, any)
                }
                is Float -> {
                    editor.putFloat(key, any)
                }
                is Long -> {
                    editor.putLong(key, any)
                }
                else -> {
                    editor.putString(key, any.toString())
                }
            }
            editor.apply()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    /**
     * 获取保存的数据
     */
    operator fun getValue(key: String?, defaultObject: Any?): Any? {
        try {
            return when (defaultObject) {
                is String -> {
                    sharedPreferences!!.getString(key, defaultObject as String?)
                }
                is Int -> {
                    sharedPreferences!!.getInt(key, (defaultObject as Int?)!!)
                }
                is Boolean -> {
                    sharedPreferences!!.getBoolean(key, (defaultObject as Boolean?)!!)
                }
                is Float -> {
                    sharedPreferences!!.getFloat(key, (defaultObject as Float?)!!)
                }
                is Long -> {
                    sharedPreferences!!.getLong(key, (defaultObject as Long?)!!)
                }
                else -> {
                    sharedPreferences!!.getString(key, null)
                }
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 移除某个key值已经对应的值
     */
    fun removeKey(key: String?) {
        try {
            sharedPreferences!!.edit().remove(key).apply()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    /**
     * 清除所有数据
     */
    fun clearAll() {
        try {
            sharedPreferences!!.edit().clear().apply()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    /**
     * 查询某个key是否存在
     */
    fun containsKey(key: String?): Boolean {
        try {
            return sharedPreferences!!.contains(key)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return false
    }
}