package com.pengxh.kt.lib.fragments.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentWidgetCircleProgressBarBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment

class CircleProgressBarFragment : KotlinBaseFragment<FragmentWidgetCircleProgressBarBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetCircleProgressBarBinding {
        return FragmentWidgetCircleProgressBarBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        binding.circleProgressBar.setCurrentValue(95)
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}