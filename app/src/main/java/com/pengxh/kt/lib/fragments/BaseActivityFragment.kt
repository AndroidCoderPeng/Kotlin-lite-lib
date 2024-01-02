package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentBaseActivityBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment

class BaseActivityFragment : KotlinBaseFragment<FragmentBaseActivityBinding>(){

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBaseActivityBinding {
        return FragmentBaseActivityBinding.inflate(inflater, container, false)
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