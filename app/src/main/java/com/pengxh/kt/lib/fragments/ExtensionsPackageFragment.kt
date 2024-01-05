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
import com.pengxh.kt.lib.fragments.extensions.DialogExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.DrawableExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.FileExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.FloatExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.FragmentExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.ImageExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.ImageViewExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.IntExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.LongExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.StringExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.TextSwitcherExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.UriExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.ViewBindingExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.ViewExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.ViewModelExtensionFragment
import com.pengxh.kt.lite.base.KotlinBaseFragment

class ExtensionsPackageFragment : KotlinBaseFragment<FragmentExtensionsPackageBinding>() {

    private lateinit var slideAdapter: SlideAdapter
    private val itemTitles = arrayOf(
        "Activity",
        "Any",
        "Bitmap",
        "ByteArray",
        "Context",
        "Dialog",
        "Drawable",
        "File",
        "Float",
        "Fragment",
        "Image",
        "ImageView",
        "Int",
        "Long",
        "String",
        "TextSwitcher",
        "Uri",
        "View",
        "ViewBinding"
    )
    private var fragmentPages: ArrayList<Fragment> = ArrayList()

    init {
        fragmentPages.add(ActivityExtensionFragment())
        fragmentPages.add(AnyExtensionFragment())
        fragmentPages.add(BitmapExtensionFragment())
        fragmentPages.add(ByteArrayExtensionFragment())
        fragmentPages.add(ContextExtensionFragment())
        fragmentPages.add(DialogExtensionFragment())
        fragmentPages.add(DrawableExtensionFragment())
        fragmentPages.add(FileExtensionFragment())
        fragmentPages.add(FloatExtensionFragment())
        fragmentPages.add(FragmentExtensionFragment())
        fragmentPages.add(ImageExtensionFragment())
        fragmentPages.add(ImageViewExtensionFragment())
        fragmentPages.add(IntExtensionFragment())
        fragmentPages.add(LongExtensionFragment())
        fragmentPages.add(StringExtensionFragment())
        fragmentPages.add(TextSwitcherExtensionFragment())
        fragmentPages.add(UriExtensionFragment())
        fragmentPages.add(ViewExtensionFragment())
        fragmentPages.add(ViewBindingExtensionFragment())
        fragmentPages.add(ViewModelExtensionFragment())
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