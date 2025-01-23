package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentWidgetPackageBinding
import com.pengxh.kt.lib.fragments.widget.AirDashBoardViewFragment
import com.pengxh.kt.lib.fragments.widget.AudioFragment
import com.pengxh.kt.lib.fragments.widget.CircleProgressBarFragment
import com.pengxh.kt.lib.fragments.widget.DeleteEditTextFragment
import com.pengxh.kt.lib.fragments.widget.DialogFragment
import com.pengxh.kt.lib.fragments.widget.EasyPopupWindowFragment
import com.pengxh.kt.lib.fragments.widget.EmptyViewFragment
import com.pengxh.kt.lib.fragments.widget.KeyBoardViewFragment
import com.pengxh.kt.lib.fragments.widget.SlideBarViewFragment
import com.pengxh.kt.lib.fragments.widget.SteeringWheelViewFragment
import com.pengxh.kt.lib.fragments.widget.TitleBarViewFragment
import com.pengxh.kt.lite.base.KotlinBaseFragment

class WidgetPackageFragment : KotlinBaseFragment<FragmentWidgetPackageBinding>() {

    private val itemTitles = arrayOf(
        "音频录制及播放",
        "对话框",
        "空气指数表盘",
        "圆形进度条",
        "带删除的输入框",
        "PopupWindow",
        "空白页面",
        "数字键盘",
        "联系人侧边栏",
        "方向控制盘",
        "顶部标题栏"
    )
    private val fragmentPages = mutableListOf<Fragment>()

    private fun getFragmentAt(position: Int): Fragment {
        if (position < fragmentPages.size) {
            return fragmentPages[position]
        }
        val fragment = when (position) {
            0 -> AudioFragment()
            1 -> DialogFragment()
            2 -> AirDashBoardViewFragment()
            3 -> CircleProgressBarFragment()
            4 -> DeleteEditTextFragment()
            5 -> EasyPopupWindowFragment()
            6 -> EmptyViewFragment()
            7 -> KeyBoardViewFragment()
            8 -> SlideBarViewFragment()
            9 -> SteeringWheelViewFragment()
            10 -> TitleBarViewFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
        fragmentPages.add(fragment)
        return fragment
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetPackageBinding {
        return FragmentWidgetPackageBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        binding.spinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, itemTitles)
        binding.spinner.setSelection(0)
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                switchPage(getFragmentAt(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }

    private fun switchPage(description: Fragment) {
        val transition = childFragmentManager.beginTransaction()
        transition.replace(R.id.contentLayout, description)
        transition.commit()
    }
}