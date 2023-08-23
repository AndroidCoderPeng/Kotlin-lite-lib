package com.pengxh.kt.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pengxh.kt.lib.databinding.ActivityMainBinding
import com.pengxh.kt.lite.extensions.binding
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.widget.SteeringWheelView

class MainActivity : AppCompatActivity() {

    private val kTag = "MainActivity"
    private val context = this@MainActivity
    private val binding: ActivityMainBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.steeringWheelView.setOnWheelTouchListener(object :
            SteeringWheelView.OnWheelTouchListener {
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