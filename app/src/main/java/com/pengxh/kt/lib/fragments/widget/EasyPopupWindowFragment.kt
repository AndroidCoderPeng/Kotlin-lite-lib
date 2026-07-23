package com.pengxh.kt.lib.fragments.widget

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.pengxh.kt.lib.databinding.FragmentWidgetEasyPopupWindowBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.widget.EasyPopupWindow

class EasyPopupWindowFragment : KotlinBaseFragment<FragmentWidgetEasyPopupWindowBinding>() {

    private val kTag = "EasyPopupWindowFragment"
    private var easyPopupWindow: EasyPopupWindow? = null

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

        val screenWidth = requireContext().resources.displayMetrics.widthPixels
        val popupWidth = (screenWidth * 0.5f).toInt()
        easyPopupWindow = EasyPopupWindow(requireContext(), popupWidth)
        easyPopupWindow?.set(menuItems) { popup, position ->
            Log.d(kTag, "onPopupItemClicked => ${menuItems[position].name}")
            easyPopupWindow?.let { popup ->

            }

            popup.contentView.animate()
                .scaleY(0f)
                .alpha(0f)
                .setDuration(300)
                .setInterpolator(AccelerateInterpolator())
                .withEndAction { popup.dismiss() }
                .start()
        }
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.showPopupButton.setOnClickListener {
            val screenWidth = requireContext().resources.displayMetrics.widthPixels
            easyPopupWindow?.let { popup ->
                popup.contentView.scaleY = 0f
                popup.contentView.pivotY = 0f
                popup.contentView.alpha = 0f

                val xoff = (screenWidth - popup.width) / 2
                popup.showAsDropDown(it, xoff, 0, Gravity.CENTER_HORIZONTAL)

                popup.contentView.post {
                    popup.contentView.animate()
                        .scaleY(1f)
                        .alpha(1f)
                        .setDuration(250)
                        .setInterpolator(DecelerateInterpolator())
                        .start()
                }
            }
        }
    }
}