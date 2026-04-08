package com.pengxh.kt.lib.view

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.pengxh.kt.lib.adapter.ViewPagerAdapter
import com.pengxh.kt.lib.databinding.ActivityMainBinding
import com.pengxh.kt.lib.fragments.AdapterPackageFragment
import com.pengxh.kt.lib.fragments.DividerPackageFragment
import com.pengxh.kt.lib.fragments.ExtensionsPackageFragment
import com.pengxh.kt.lib.fragments.UtilsPackageFragment
import com.pengxh.kt.lib.fragments.WidgetPackageFragment
import com.pengxh.kt.lite.base.KotlinBaseActivity

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
        ViewCompat.setOnApplyWindowInsetsListener(binding.titleView) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(0, statusBarHeight, 0, 0)
            insets
        }
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