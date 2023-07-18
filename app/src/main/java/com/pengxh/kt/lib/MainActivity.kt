package com.pengxh.kt.lib

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pengxh.kt.lite.base.KotlinBaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : KotlinBaseActivity() {

    private val kTag = "MainActivity"
    private var fragmentList = ArrayList<Fragment>()

    init {
        fragmentList.add(FirstFragment())
    }

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun setupTopBarLayout() {

    }

    override fun observeRequestState() {

    }

    override fun initData(savedInstanceState: Bundle?) {
        mainViewPager.adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
    }

    override fun initEvent() {

    }

    inner class ViewPagerAdapter(fm: FragmentManager?, private val mFragments: List<Fragment>) :
        FragmentPagerAdapter(fm!!) {
        override fun getItem(i: Int): Fragment {
            return mFragments[i]
        }

        override fun getCount(): Int {
            return mFragments.size
        }
    }
}