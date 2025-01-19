package com.pengxh.kt.lib.fragments.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentWidgetEmptyViewBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.widget.EmptyView

class EmptyViewFragment : KotlinBaseFragment<FragmentWidgetEmptyViewBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetEmptyViewBinding {
        return FragmentWidgetEmptyViewBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.emptyView.setOnClickListener(object : EmptyView.OnClickListener {
            override fun onReloadButtonClicked() {
                "重新加载".show(requireContext())
            }
        })
    }
}