package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentExtensionActivityBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment

/**
 * Activity委托方式初始化View，省去findViewById操作
 * */
class ActivityExtensionFragment : KotlinBaseFragment<FragmentExtensionActivityBinding>() {

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionActivityBinding {
        return FragmentExtensionActivityBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}