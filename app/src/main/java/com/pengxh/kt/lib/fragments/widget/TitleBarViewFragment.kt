package com.pengxh.kt.lib.fragments.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentWidgetTitleBarViewBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.widget.TitleBarView

class TitleBarViewFragment : KotlinBaseFragment<FragmentWidgetTitleBarViewBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetTitleBarViewBinding {
        return FragmentWidgetTitleBarViewBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {
        binding.titleView.setOnClickListener(object : TitleBarView.OnClickListener {
            override fun onLeftClick() {
                "onLeftClick".show(requireContext())
            }

            override fun onRightClick() {
                binding.titleView.getTitle().show(requireContext())
            }
        })
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}