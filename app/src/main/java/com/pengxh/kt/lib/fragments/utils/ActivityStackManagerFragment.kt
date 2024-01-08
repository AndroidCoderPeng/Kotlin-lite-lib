package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentUtilsActivityStackManagerBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment

class ActivityStackManagerFragment :
    KotlinBaseFragment<FragmentUtilsActivityStackManagerBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsActivityStackManagerBinding {
        return FragmentUtilsActivityStackManagerBinding.inflate(inflater, container, false)
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