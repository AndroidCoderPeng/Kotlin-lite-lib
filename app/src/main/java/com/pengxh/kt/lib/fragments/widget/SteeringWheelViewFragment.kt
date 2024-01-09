package com.pengxh.kt.lib.fragments.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentWidgetSteeringWheelViewBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.widget.SteeringWheelView

class SteeringWheelViewFragment : KotlinBaseFragment<FragmentWidgetSteeringWheelViewBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetSteeringWheelViewBinding {
        return FragmentWidgetSteeringWheelViewBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.steeringWheelView.setOnWheelTouchListener(object :
            SteeringWheelView.OnWheelTouchListener {
            override fun onCenterClicked() {
                "onCenterClicked: 点击".show(requireContext())
            }

            override fun onLeftTurn() {
                "onLeftTurn: 按下".show(requireContext())
            }

            override fun onTopTurn() {
                "onTopTurn: 按下".show(requireContext())
            }

            override fun onRightTurn() {
                "onRightTurn: 按下".show(requireContext())
            }

            override fun onBottomTurn() {
                "onBottomTurn: 按下".show(requireContext())
            }

            override fun onActionTurnUp(dir: SteeringWheelView.Direction) {
                "onActionTurnUp: 松开${dir.name}".show(requireContext())
            }
        })
    }
}