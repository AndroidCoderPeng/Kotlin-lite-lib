package com.pengxh.kt.lib.fragments.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentWidgetDeleteEditTextBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment

class DeleteEditTextFragment : KotlinBaseFragment<FragmentWidgetDeleteEditTextBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetDeleteEditTextBinding {
        return FragmentWidgetDeleteEditTextBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}