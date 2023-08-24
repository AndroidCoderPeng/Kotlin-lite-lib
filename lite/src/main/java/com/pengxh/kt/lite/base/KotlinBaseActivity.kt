package com.pengxh.kt.lite.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class KotlinBaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = initViewBinding()
        setContentView(binding.root)
        setupTopBarLayout()
        initOnCreate(savedInstanceState)
        observeRequestState()
        initEvent()
    }

    /**
     * 初始化ViewBinding
     */
    abstract fun initViewBinding(): VB

    /**
     * 特定页面定制沉浸式状态栏
     */
    abstract fun setupTopBarLayout()

    /**
     * 初始化默认数据
     */
    abstract fun initOnCreate(savedInstanceState: Bundle?)

    /**
     * 数据请求状态监听
     */
    abstract fun observeRequestState()

    /**
     * 初始化业务逻辑
     */
    abstract fun initEvent()
}