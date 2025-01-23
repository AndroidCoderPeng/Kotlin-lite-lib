package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentDividerPackageBinding
import com.pengxh.kt.lib.fragments.divider.RecyclerStickDecorationFragment
import com.pengxh.kt.lib.fragments.divider.RecyclerViewItemDividerFragment
import com.pengxh.kt.lib.fragments.divider.RecyclerViewItemOffsetsFragment
import com.pengxh.kt.lite.base.KotlinBaseFragment

class DividerPackageFragment : KotlinBaseFragment<FragmentDividerPackageBinding>() {

    private val itemTitles = arrayOf(
        "吸顶分割线", "普通分割线", "四周边框线"
    )
    private val fragmentPages = mutableListOf<Fragment>()

    private fun getFragmentAt(position: Int): Fragment {
        if (position < fragmentPages.size) {
            return fragmentPages[position]
        }
        val fragment = when (position) {
            0 -> RecyclerStickDecorationFragment()
            1 -> RecyclerViewItemDividerFragment()
            2 -> RecyclerViewItemOffsetsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
        fragmentPages.add(fragment)
        return fragment
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDividerPackageBinding {
        return FragmentDividerPackageBinding.inflate(inflater, container, false)
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