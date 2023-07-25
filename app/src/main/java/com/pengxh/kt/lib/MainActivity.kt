package com.pengxh.kt.lib

import android.os.Bundle
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.widget.SteeringWheelView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : KotlinBaseActivity() {

    private val kTag = "MainActivity"
    private val context = this@MainActivity

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun setupTopBarLayout() {

    }

    override fun observeRequestState() {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initEvent() {
        steeringWheelView.setOnWheelTouchListener(object : SteeringWheelView.OnWheelTouchListener {
            override fun onCenterClicked() {
                "onCenterClicked".show(context)
            }

            override fun onLeftTurn() {
                "onLeftTurn".show(context)
            }

            override fun onTopTurn() {
                "onTopTurn".show(context)
            }

            override fun onRightTurn() {
                "onRightTurn".show(context)
            }

            override fun onBottomTurn() {
                "onBottomTurn".show(context)
            }

            override fun onActionTurnUp(dir: SteeringWheelView.Direction) {
                when (dir) {
                    SteeringWheelView.Direction.LEFT -> {
                        "LEFT onActionTurnUp".show(context)
                    }
                    SteeringWheelView.Direction.TOP -> {
                        "TOP onActionTurnUp".show(context)
                    }
                    SteeringWheelView.Direction.RIGHT -> {
                        "RIGHT onActionTurnUp".show(context)
                    }
                    SteeringWheelView.Direction.BOTTOM -> {
                        "BOTTOM onActionTurnUp".show(context)
                    }
                }
            }
        })
    }
}