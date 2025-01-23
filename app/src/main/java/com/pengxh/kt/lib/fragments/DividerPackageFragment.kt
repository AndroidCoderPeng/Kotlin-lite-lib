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
    private var fragmentPages: ArrayList<Fragment> = ArrayList()

    init {
        fragmentPages.add(RecyclerStickDecorationFragment())
        fragmentPages.add(RecyclerViewItemDividerFragment())
        fragmentPages.add(RecyclerViewItemOffsetsFragment())
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
                switchPage(fragmentPages[position])
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