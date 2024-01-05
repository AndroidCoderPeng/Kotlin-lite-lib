package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentExtensionIntBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.convertColor
import com.pengxh.kt.lite.extensions.convertDrawable
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.extensions.getScreenDensity
import com.pengxh.kt.lite.extensions.px2dp
import com.pengxh.kt.lite.extensions.sp2px

class IntExtensionFragment : KotlinBaseFragment<FragmentExtensionIntBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionIntBinding {
        return FragmentExtensionIntBinding.inflate(inflater, container, false)
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
                0
            } else {
                binding.pxInputView.text.toString().toInt()
            }

            val dp = if (binding.dpInputView.text.isNullOrBlank()) {
                0
            } else {
                binding.dpInputView.text.toString().toInt()
            }

            val sp = if (binding.spInputView.text.isNullOrBlank()) {
                0
            } else {
                binding.spInputView.text.toString().toInt()
            }

            binding.pxResultView.text = px.px2dp(requireContext()).toString()
            binding.dpResultView.text = dp.dp2px(requireContext()).toString()
            binding.spResultView.text = sp.sp2px(requireContext()).toString()
        }

        binding.colorButton.setOnClickListener {
            binding.colorResLayout.setBackgroundColor(R.color.green.convertColor(requireContext()))
        }

        binding.drawableButton.setOnClickListener {
            binding.drawableView.setImageDrawable(
                R.drawable.load_imag_error.convertDrawable(requireContext())
            )
        }
    }
}