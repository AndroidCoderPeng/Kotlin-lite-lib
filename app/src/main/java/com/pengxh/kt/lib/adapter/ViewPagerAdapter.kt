package com.pengxh.kt.lib.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(
    manager: FragmentManager,
    private val pages: ArrayList<Fragment>,
    private val titles: Array<String>
) : FragmentPagerAdapter(manager) {
    override fun getItem(position: Int): Fragment = pages[position]

    override fun getCount(): Int = pages.size

    override fun getPageTitle(position: Int): CharSequence = titles[position]
}