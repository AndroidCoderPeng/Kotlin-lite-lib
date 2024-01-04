package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentFloatExtensionBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.extensions.getScreenDensity
import com.pengxh.kt.lite.extensions.px2dp
import com.pengxh.kt.lite.extensions.sp2px

class FloatExtensionFragment : KotlinBaseFragment<FragmentFloatExtensionBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFloatExtensionBinding {
        return FragmentFloatExtensionBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        binding.screenDensityView.text = "${requireContext().getScreenDensity()}"
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.convertButton.setOnClickListener {
            val px = if (binding.pxInputView.text.isNullOrBlank()) {
                0f
            } else {
                binding.pxInputView.text.toString().toFloat()
            }

            val dp = if (binding.dpInputView.text.isNullOrBlank()) {
                0f
            } else {
                binding.dpInputView.text.toString().toFloat()
            }

            val sp = if (binding.spInputView.text.isNullOrBlank()) {
                0f
            } else {
                binding.spInputView.text.toString().toFloat()
            }

            binding.pxResultView.text = px.px2dp(requireContext()).toString()
            binding.dpResultView.text = dp.dp2px(requireContext()).toString()
            binding.spResultView.text = sp.sp2px(requireContext()).toString()
        }
    }
}