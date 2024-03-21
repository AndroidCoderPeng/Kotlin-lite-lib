package com.pengxh.kt.lib.fragments.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentWidgetAirDashBoardViewBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment

class AirDashBoardViewFragment : KotlinBaseFragment<FragmentWidgetAirDashBoardViewBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetAirDashBoardViewBinding {
        return FragmentWidgetAirDashBoardViewBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        binding.airDashBoardView.setCenterText("差").setCurrentValue(455)
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}