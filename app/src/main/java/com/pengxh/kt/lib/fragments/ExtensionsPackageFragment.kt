package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.adapter.SlideAdapter
import com.pengxh.kt.lib.databinding.FragmentExtensionsPackageBinding
import com.pengxh.kt.lib.fragments.extensions.ActivityExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.AnyExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.BitmapExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.ByteArrayExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.ContextExtensionFragment
import com.pengxh.kt.lite.base.KotlinBaseFragment

class ExtensionsPackageFragment : KotlinBaseFragment<FragmentExtensionsPackageBinding>() {

    private lateinit var slideAdapter: SlideAdapter
    private val itemTitles = arrayOf(
        "Activity", "Any", "Bitmap", "ByteArray", "Context"
    )
    private var fragmentPages: ArrayList<Fragment> = ArrayList()

    init {
        fragmentPages.add(ActivityExtensionFragment())
        fragmentPages.add(AnyExtensionFragment())
        fragmentPages.add(BitmapExtensionFragment())
        fragmentPages.add(ByteArrayExtensionFragment())
        fragmentPages.add(ContextExtensionFragment())
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionsPackageBinding {
        return FragmentExtensionsPackageBinding.inflate(inflater, container, false)
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