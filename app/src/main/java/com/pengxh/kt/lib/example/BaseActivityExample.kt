package com.pengxh.kt.lib.example

import android.os.Bundle
import com.pengxh.kt.lib.databinding.ActivityExampleBaseActivityBinding
import com.pengxh.kt.lite.base.KotlinBaseActivity

class BaseActivityExample : KotlinBaseActivity<ActivityExampleBaseActivityBinding>() {
    override fun initViewBinding(): ActivityExampleBaseActivityBinding {
        return ActivityExampleBaseActivityBinding.inflate(layoutInflater)
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