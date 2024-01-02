package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.adapter.SlideAdapter
import com.pengxh.kt.lib.databinding.FragmentAdapterPackageBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment

class AdapterPackageFragment : KotlinBaseFragment<FragmentAdapterPackageBinding>() {

    private lateinit var slideAdapter: SlideAdapter
    private val itemTitles = arrayOf(
        "可变适配器", "多选适配器", "普通适配器", "只读适配器", "单选适配器"
    )
    private var fragmentPages: ArrayList<Fragment> = ArrayList()

    init {
        fragmentPages.add(EditableImageAdapterFragment())
        fragmentPages.add(MultipleChoiceAdapterFragment())
        fragmentPages.add(NormalRecyclerAdapterFragment())
        fragmentPages.add(ReadOnlyImageAdapterFragment())
//        fragmentPages.add(SingleChoiceAdapterFragment())
    }

    override fun initViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentAdapterPackageBinding {
        return FragmentAdapterPackageBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        slideAdapter = SlideAdapter(requireContext(), itemTitles)
        binding.listView.adapter = slideAdapter

        //默认选中第一个
        slideAdapter.setSelectItem(0)
        slideAdapter.notifyDataSetInvalidated()

        //显示首页
        switchPage(fragmentPages[0])
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.listView.setOnItemClickListener { _, _, position, _ ->
            slideAdapter.setSelectItem(position)
            slideAdapter.notifyDataSetInvalidated()

            //切换页面
            switchPage(fragmentPages[position])
        }
    }

    private fun switchPage(description: Fragment) {
        val transition = childFragmentManager.beginTransaction()
        transition.replace(R.id.contentLayout, description)
        transition.commit()
    }
}