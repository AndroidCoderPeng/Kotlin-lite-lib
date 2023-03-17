package com.pengxh.kt.lite.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class KotlinBaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initLayoutView())
        setupTopBarLayout()
        initData()
        observeRequestState()
        initEvent()
    }

    /**
     * 初始化xml布局
     */
    abstract fun initLayoutView(): Int

    /**
     * 特定页面定制沉浸式状态栏
     */
    abstract fun setupTopBarLayout()

    /**
     * 初始化默认数据
     */
    abstract fun initData()

    /**
     * 数据请求状态监听
     */
    abstract fun observeRequestState()

    /**
     * 初始化业务逻辑
     */
    abstract fun initEvent()
}