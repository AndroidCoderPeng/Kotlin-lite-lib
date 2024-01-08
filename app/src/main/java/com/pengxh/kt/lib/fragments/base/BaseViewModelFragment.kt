package com.pengxh.kt.lib.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentBaseViewModelBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment

class BaseViewModelFragment : KotlinBaseFragment<FragmentBaseViewModelBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBaseViewModelBinding {
        return FragmentBaseViewModelBinding.inflate(inflater, container, false)
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