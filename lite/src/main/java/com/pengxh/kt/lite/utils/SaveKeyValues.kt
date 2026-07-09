package com.pengxh.kt.lite.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SaveKeyValues {
    private lateinit var sp: SharedPreferences

    fun initSharedPreferences(context: Context) {
        val packageName = context.packageName
        //获取到的包名带有“.”方便命名，取最后一个作为sp文件名
        val split = packageName.split(".")
        val fileName = if (split.isNotEmpty()) split.last() else "default_prefs"
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    // ==================== put ====================
    @Deprecated("请使用基本类型put")
    fun putValue(key: String, any: Any?) {
        if (key.isBlank()) {
            return
        }
        if (any == null) {
            removeKey(key)
            return
        }
        when (any) {
            is String -> putString(key, any)
            is Int -> putInt(key, any)
            is Boolean -> putBoolean(key, any)
            is Float -> putFloat(key, any)
            is Long -> putLong(key, any)
            else -> putString(key, any.toString())
        }
    }

    fun putString(key: String, value: String) {
        if (key.isBlank()) return
        try {
            sp.edit { putString(key, value) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun putInt(key: String, value: Int) {
        if (key.isBlank()) return
        try {
            sp.edit { putInt(key, value) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun putLong(key: String, value: Long) {
        if (key.isBlank()) return
        try {
            sp.edit { putLong(key, value) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun putFloat(key: String, value: Float) {
        if (key.isBlank()) return
        try {
            sp.edit { putFloat(key, value) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun putBoolean(key: String, value: Boolean) {
        if (key.isBlank()) return
        try {
            sp.edit { putBoolean(key, value) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ==================== get ====================
    @Deprecated("请使用基本类型get")
    fun getValue(key: String, defaultObject: Any): Any? {
        if (key.isBlank()) {
            return null
        }
        return when (defaultObject) {
            is String -> getString(key, defaultObject)
            is Int -> getInt(key, defaultObject)
            is Boolean -> getBoolean(key, defaultObject)
            is Float -> getFloat(key, defaultObject)
            is Long -> getLong(key, defaultObject)
            else -> defaultObject
        }
    }

    fun getString(key: String, defaultValue: String = ""): String {
        if (key.isBlank()) return defaultValue
        return sp.getString(key, defaultValue) ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        if (key.isBlank()) return defaultValue
        return sp.getInt(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        if (key.isBlank()) return defaultValue
        return sp.getLong(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        if (key.isBlank()) return defaultValue
        return sp.getFloat(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        if (key.isBlank()) return defaultValue
        return sp.getBoolean(key, defaultValue)
    }

    /**
     * 移除某个key和value
     */
    fun removeKey(key: String) {
        if (key.isBlank()) return
        try {
            sp.edit { remove(key) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 清除所有数据
     */
    fun clearAll() {
        try {
            sp.edit { clear() }
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