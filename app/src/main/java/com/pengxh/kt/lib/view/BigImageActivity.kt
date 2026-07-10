package com.pengxh.kt.lib.view

import android.os.Bundle
import com.pengxh.kt.lib.databinding.ActivityBigImageBinding
import com.pengxh.kt.lite.base.KotlinBaseActivity

// TODO 查看单张/一系列大图
class BigImageActivity : KotlinBaseActivity<ActivityBigImageBinding>() {
    override fun initViewBinding(): ActivityBigImageBinding {
        return ActivityBigImageBinding.inflate(layoutInflater)
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