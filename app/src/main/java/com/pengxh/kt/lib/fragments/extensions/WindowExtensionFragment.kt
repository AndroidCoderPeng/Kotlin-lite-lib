package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import com.pengxh.kt.lib.databinding.FragmentExtensionWindowBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.setScreenBrightness

class WindowExtensionFragment : KotlinBaseFragment<FragmentExtensionWindowBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionWindowBinding {
        return FragmentExtensionWindowBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        requireActivity().window.setScreenBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL)
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.setScreenBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE)
    }
}