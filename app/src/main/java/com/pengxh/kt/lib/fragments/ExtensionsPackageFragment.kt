package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentExtensionsPackageBinding
import com.pengxh.kt.lib.fragments.extensions.BitmapExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.ByteArrayExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.ContextExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.DialogExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.DrawableExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.FileExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.FloatExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.ImageExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.ImageViewExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.IntExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.LongExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.StringExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.TextSwitcherExtensionFragment
import com.pengxh.kt.lib.fragments.extensions.UriExtensionFragment
import com.pengxh.kt.lite.base.KotlinBaseFragment

class ExtensionsPackageFragment : KotlinBaseFragment<FragmentExtensionsPackageBinding>() {

    private val itemTitles = arrayOf(
        "Bitmap",
        "ByteArray",
        "Context",
        "Dialog",
        "Drawable",
        "File",
        "Float",
        "Image",
        "ImageView",
        "Int",
        "Long",
        "String",
        "TextSwitcher",
        "Uri"
    )
    private var fragmentPages: ArrayList<Fragment> = ArrayList()

    init {
        fragmentPages.add(BitmapExtensionFragment())
        fragmentPages.add(ByteArrayExtensionFragment())
        fragmentPages.add(ContextExtensionFragment())
        fragmentPages.add(DialogExtensionFragment())
        fragmentPages.add(DrawableExtensionFragment())
        fragmentPages.add(FileExtensionFragment())
        fragmentPages.add(FloatExtensionFragment())
        fragmentPages.add(ImageExtensionFragment())
        fragmentPages.add(ImageViewExtensionFragment())
        fragmentPages.add(IntExtensionFragment())
        fragmentPages.add(LongExtensionFragment())
        fragmentPages.add(StringExtensionFragment())
        fragmentPages.add(TextSwitcherExtensionFragment())
        fragmentPages.add(UriExtensionFragment())
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