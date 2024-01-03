package com.pengxh.kt.lib.example

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pengxh.kt.lib.databinding.ActivityActivityExtensionExampleBinding
import com.pengxh.kt.lite.extensions.binding

class ActivityExtensionExample : AppCompatActivity() {

    //绑定布局，替换setContentView，省去findViewById操作
    private val binding by binding<ActivityActivityExtensionExampleBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.rootView.setBackgroundColor(Color.BLUE)
    }
}