package com.pengxh.kt.lite.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class KotlinBaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(initLayoutView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopBarLayout()
        initData(savedInstanceState)
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
    abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 数据请求状态监听
     */
    abstract fun observeRequestState()

    /**
     * 初始化业务逻辑
     */
    abstract fun initEvent()
}