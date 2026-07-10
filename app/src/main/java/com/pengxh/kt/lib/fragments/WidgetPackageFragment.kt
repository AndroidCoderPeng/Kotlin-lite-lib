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
import com.pengxh.kt.lib.fragments.widget.DeleteEditTextFragment
import com.pengxh.kt.lib.fragments.widget.DialogFragment
import com.pengxh.kt.lib.fragments.widget.EasyPopupWindowFragment
import com.pengxh.kt.lib.fragments.widget.EmptyViewFragment
import com.pengxh.kt.lib.fragments.widget.KeyBoardViewFragment
import com.pengxh.kt.lib.fragments.widget.SteeringWheelViewFragment
import com.pengxh.kt.lite.base.KotlinBaseFragment

class WidgetPackageFragment : KotlinBaseFragment<FragmentWidgetPackageBinding>() {

    private val itemTitles = arrayOf(
        "对话框",
        "带删除的输入框",
        "PopupWindow",
        "空白页面",
        "数字键盘",
        "方向控制盘"
    )
    private val fragmentPages = mutableListOf<Fragment>()

    private fun getFragmentAt(position: Int): Fragment {
        if (position < fragmentPages.size) {
            return fragmentPages[position]
        }
        val fragment = when (position) {
            0 -> DialogFragment()
            1 -> DeleteEditTextFragment()
            2 -> EasyPopupWindowFragment()
            3 -> EmptyViewFragment()
            4 -> KeyBoardViewFragment()
            5 -> SteeringWheelViewFragment()
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