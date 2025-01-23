package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentAdapterPackageBinding
import com.pengxh.kt.lib.fragments.adapter.EditableImageAdapterFragment
import com.pengxh.kt.lib.fragments.adapter.GridViewImageAdapterFragment
import com.pengxh.kt.lib.fragments.adapter.MultipleChoiceAdapterFragment
import com.pengxh.kt.lib.fragments.adapter.NormalRecyclerAdapterFragment
import com.pengxh.kt.lib.fragments.adapter.SingleChoiceAdapterFragment
import com.pengxh.kt.lite.base.KotlinBaseFragment


class AdapterPackageFragment : KotlinBaseFragment<FragmentAdapterPackageBinding>() {

    private val itemTitles = arrayOf(
        "可变适配器", "多选适配器", "普通适配器", "宫格适配器", "单选适配器"
    )
    private var fragmentPages: ArrayList<Fragment> = ArrayList()

    init {
        fragmentPages.add(EditableImageAdapterFragment())
        fragmentPages.add(MultipleChoiceAdapterFragment())
        fragmentPages.add(NormalRecyclerAdapterFragment())
        fragmentPages.add(GridViewImageAdapterFragment())
        fragmentPages.add(SingleChoiceAdapterFragment())
    }

    override fun initViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentAdapterPackageBinding {
        return FragmentAdapterPackageBinding.inflate(inflater, container, false)
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