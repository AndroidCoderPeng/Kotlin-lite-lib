package com.pengxh.kt.lib.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pengxh.kt.lib.databinding.ActivityExampleActivityExtensionBinding
import com.pengxh.kt.lite.extensions.binding
import com.pengxh.kt.lite.utils.Constant

class ActivityExtensionExample : AppCompatActivity() {

    //绑定布局，替换setContentView，省去findViewById操作
    private val binding by binding<ActivityExampleActivityExtensionBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.backButton.setOnClickListener { finish() }

        val args = intent.getStringArrayListExtra(Constant.INTENT_PARAM_KEY)
        if (args == null) {
            val extra = intent.getStringExtra(Constant.INTENT_PARAM_KEY)
            binding.textView.text = extra
        } else {
            val builder = StringBuilder()
            args.forEach {
                builder.append(it).append("\r\n")
            }
            binding.textView.text = builder.toString()
        }
    }
}