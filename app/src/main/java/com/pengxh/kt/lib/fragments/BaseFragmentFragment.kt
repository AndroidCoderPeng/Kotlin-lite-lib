package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentBaseFragmentBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment

class BaseFragmentFragment : KotlinBaseFragment<FragmentBaseFragmentBinding>() {

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBaseFragmentBinding {
        return FragmentBaseFragmentBinding.inflate(inflater, container, false)
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