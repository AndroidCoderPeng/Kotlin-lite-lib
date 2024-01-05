package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentViewExtensionBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.toBitmap

class ViewExtensionFragment : KotlinBaseFragment<FragmentViewExtensionBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentViewExtensionBinding {
        return FragmentViewExtensionBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val view = LayoutInflater.from(context).inflate(R.layout.map_info_window, null)
        val locationView: TextView = view.findViewById(R.id.locationView)
        locationView.text = "ViewExtensionFragment"
        val bitmap = view.toBitmap()
        binding.imageView.setImageBitmap(bitmap)
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}