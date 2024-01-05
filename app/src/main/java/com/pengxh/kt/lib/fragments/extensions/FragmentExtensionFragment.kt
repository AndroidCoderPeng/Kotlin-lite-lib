package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentExtensionFragmentBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment

/**
 * Fragment委托方式初始化View，省去findViewById操作
 * */
class FragmentExtensionFragment : KotlinBaseFragment<FragmentExtensionFragmentBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionFragmentBinding {
        return FragmentExtensionFragmentBinding.inflate(inflater, container, false)
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