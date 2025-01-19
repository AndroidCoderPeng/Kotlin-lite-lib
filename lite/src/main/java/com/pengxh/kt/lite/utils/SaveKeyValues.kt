package com.pengxh.kt.lite.utils

import android.content.Context
import android.content.SharedPreferences

object SaveKeyValues {
    private lateinit var sp: SharedPreferences

    fun initSharedPreferences(context: Context) {
        val packageName = context.packageName
        //获取到的包名带有“.”方便命名，取最后一个作为sp文件名
        val split = packageName.split(".")
        val fileName = if (split.isNotEmpty()) split.last() else "default_prefs"
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    /**
     * 存储
     */
    fun putValue(key: String, any: Any?) {
        if (key.isBlank()) {
            return
        }
        if (any == null) {
            removeKey(key)
            return
        }
        val editor = sp.edit()
        when (any) {
            is String -> editor.putString(key, any)
            is Int -> editor.putInt(key, any)
            is Boolean -> editor.putBoolean(key, any)
            is Float -> editor.putFloat(key, any)
            is Long -> editor.putLong(key, any)
            else -> editor.putString(key, any.toString())
        }
        try {
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取保存的数据
     */
    operator fun getValue(key: String, defaultObject: Any): Any? {
        if (key.isBlank()) {
            return null
        }
        return when (defaultObject) {
            is String -> sp.getString(key, defaultObject)
            is Int -> sp.getInt(key, defaultObject)
            is Boolean -> sp.getBoolean(key, defaultObject)
            is Float -> sp.getFloat(key, defaultObject)
            is Long -> sp.getLong(key, defaultObject)
            else -> defaultObject
        }
    }

    /**
     * 移除某个key和value
     */
    fun removeKey(key: String) {
        if (key.isBlank()) {
            return
        }
        try {
            sp.edit().remove(key).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 清除所有数据
     */
    fun clearAll() {
        try {
            sp.edit().clear().apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 查询某个key是否存在
     */
    fun containsKey(key: String): Boolean {
        if (key.isBlank()) {
            return false
        }
        return sp.contains(key)
    }
}