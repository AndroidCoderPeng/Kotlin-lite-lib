package com.pengxh.kt.lib.fragments.widget

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentWidgetEasyPopupWindowBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.widget.EasyPopupWindow

class EasyPopupWindowFragment : KotlinBaseFragment<FragmentWidgetEasyPopupWindowBinding>() {

    private val kTag = "EasyPopupWindowFragment"
    private val easyPopupWindow by lazy { EasyPopupWindow(requireContext()) }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetEasyPopupWindowBinding {
        return FragmentWidgetEasyPopupWindowBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val menuItems = ArrayList<EasyPopupWindow.MenuItem>()
        menuItems.add(EasyPopupWindow.MenuItem(android.R.drawable.ic_menu_compass, "第一个选项"))
        menuItems.add(EasyPopupWindow.MenuItem(android.R.drawable.ic_menu_compass, "第二个选项"))
        menuItems.add(EasyPopupWindow.MenuItem(android.R.drawable.ic_menu_compass, "第三个选项"))

        easyPopupWindow.set(menuItems, object : EasyPopupWindow.OnPopupWindowClickListener {
            override fun onPopupItemClicked(position: Int) {
                Log.d(kTag, "onPopupItemClicked => ${menuItems[position].name}")
            }
        })
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.showPopupButton.setOnClickListener {
            easyPopupWindow.showAsDropDown(it, 0, 0)
        }
    }
}