package com.pengxh.kt.lib.view

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.adapter.ViewPagerAdapter
import com.pengxh.kt.lib.databinding.ActivityMainBinding
import com.pengxh.kt.lib.fragments.AdapterPackageFragment
import com.pengxh.kt.lib.fragments.DividerPackageFragment
import com.pengxh.kt.lib.fragments.ExtensionsPackageFragment
import com.pengxh.kt.lib.fragments.UtilsPackageFragment
import com.pengxh.kt.lib.fragments.WidgetPackageFragment
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.extensions.convertColor
import com.pengxh.kt.lite.extensions.getStatusBarHeight

class MainActivity : KotlinBaseActivity<ActivityMainBinding>() {

    private val itemTitles = arrayOf(
        "adapter", "divider", "extensions", "utils", "widget"
    )
    private val fragmentPages by lazy {
        listOf(
            AdapterPackageFragment(),
            DividerPackageFragment(),
            ExtensionsPackageFragment(),
            UtilsPackageFragment(),
            WidgetPackageFragment()
        )
    }

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun setupTopBarLayout() {
        ImmersionBar.with(this)
            .statusBarDarkFont(false)
            .statusBarColorInt(R.color.mainColor.convertColor(this))
            .init()
        binding.rootView.setPadding(0, getStatusBarHeight(), 0, 0)
        binding.rootView.requestLayout()
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, fragmentPages, itemTitles)
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = fragmentPages.size
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}