package com.pengxh.kt.lib

import android.os.Bundle
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.extensions.timestampToCompleteDate
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : KotlinBaseFragment() {

    override fun initLayoutView(): Int = R.layout.fragment_first

    override fun setupTopBarLayout() {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        button.setOnClickListener {
            System.currentTimeMillis().timestampToCompleteDate().show(requireContext())
        }
    }
}