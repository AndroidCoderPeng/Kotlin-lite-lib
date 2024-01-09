package com.pengxh.kt.lib.fragments.widget

import android.graphics.Color
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
        binding.airDashBoardView
            .setMaxValue(500)
            .setCenterText("优")
            .setAirRingForeground(Color.GREEN)
            .setAirCenterTextColor(Color.RED)
            .setAirCurrentValueColor(Color.BLUE)
            .setCurrentValue(255)
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}