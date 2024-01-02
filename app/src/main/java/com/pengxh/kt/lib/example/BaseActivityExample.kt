package com.pengxh.kt.lib.example

import android.os.Bundle
import com.pengxh.kt.lib.databinding.ActivityBaseActivityExampleBinding
import com.pengxh.kt.lite.base.KotlinBaseActivity

class BaseActivityExample : KotlinBaseActivity<ActivityBaseActivityExampleBinding>() {
    override fun initViewBinding(): ActivityBaseActivityExampleBinding {
        return ActivityBaseActivityExampleBinding.inflate(layoutInflater)
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