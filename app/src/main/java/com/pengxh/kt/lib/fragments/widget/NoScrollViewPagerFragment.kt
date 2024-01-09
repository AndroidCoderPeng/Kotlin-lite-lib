package com.pengxh.kt.lib.fragments.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentWidgetNoScrollViewPagerBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment

class NoScrollViewPagerFragment : KotlinBaseFragment<FragmentWidgetNoScrollViewPagerBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetNoScrollViewPagerBinding {
        return FragmentWidgetNoScrollViewPagerBinding.inflate(inflater, container, false)
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