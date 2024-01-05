package com.pengxh.kt.lib.fragments.extensions

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.pengxh.kt.lib.databinding.FragmentTextSwitcherExtensionBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.setAnimation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Random
import java.util.Timer
import java.util.TimerTask

class TextSwitcherExtensionFragment : KotlinBaseFragment<FragmentTextSwitcherExtensionBinding>() {

    private val random by lazy { Random() }
    private val timer by lazy { Timer() }
    private val tips = mutableListOf(
        "我们不生产水，我们只是大自然的搬运工",
        "曾是梦想家，梦没了，只剩想家",
        "将所有一言难尽，一饮而尽",
        "你懂得越多，能懂你的就越少",
        "你的能量超乎你想象",
        "什么都有，什么都卖，什么都不奇怪！"
    )

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTextSwitcherExtensionBinding {
        return FragmentTextSwitcherExtensionBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        binding.textSwitcherView.setFactory {
            val textView = TextView(requireContext())
            textView.setTextColor(Color.BLUE)
            textView.textSize = 18f
            textView.typeface = Typeface.DEFAULT_BOLD
            textView
        }
        timer.schedule(object : TimerTask() {
            override fun run() {
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.textSwitcherView.setText(tips[random.nextInt(5)])
                }
            }
        }, 100, 2000)
        binding.textSwitcherView.setAnimation()
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}