package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentAdapterPackageBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment

class AdapterPackageFragment : KotlinBaseFragment<FragmentAdapterPackageBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentAdapterPackageBinding {
        return FragmentAdapterPackageBinding.inflate(inflater, container, false)
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