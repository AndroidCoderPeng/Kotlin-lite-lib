package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentViewBindingExtensionBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment

class ViewBindingExtensionFragment : KotlinBaseFragment<FragmentViewBindingExtensionBinding>() {

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentViewBindingExtensionBinding {
        return FragmentViewBindingExtensionBinding.inflate(inflater, container, false)
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