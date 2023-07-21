package com.pengxh.kt.lite.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class KotlinBaseFragment : Fragment() {

    lateinit var bv: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        bv = inflater.inflate(initLayoutRes(), container, false)
        initView(savedInstanceState)
        setupTopBarLayout()
        return bv
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeRequestState()
        initEvent()
    }

    @LayoutRes
    abstract fun initLayoutRes(): Int

    /**
     * 沉浸式状态栏
     */
    abstract fun setupTopBarLayout()

    /**
     * 初始化布局以及控件
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 网络请求状态监听
     */
    abstract fun observeRequestState()

    /**
     * 业务逻辑，按钮等事件
     */
    abstract fun initEvent()
}