package com.pengxh.kt.lib.fragments.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentWidgetKeyBoardViewBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.widget.KeyBoardView

class KeyBoardViewFragment : KotlinBaseFragment<FragmentWidgetKeyBoardViewBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetKeyBoardViewBinding {
        return FragmentWidgetKeyBoardViewBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.keyBoardView.setKeyboardClickListener(object : KeyBoardView.KeyboardClickListener {
            override fun onClick(value: String) {
                value.show(requireContext())
            }

            override fun onDelete() {
                "删除".show(requireContext())
            }
        })
    }
}