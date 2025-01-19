package com.pengxh.kt.lib.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    manager: FragmentManager, lifecycle: Lifecycle,
    private val pages: ArrayList<Fragment>,
    private val titles: Array<String>
) : FragmentStateAdapter(manager, lifecycle) {
    override fun createFragment(position: Int): Fragment = pages[position]

    override fun getItemCount(): Int = pages.size

    fun getPageTitle(position: Int): CharSequence = titles[position]
}