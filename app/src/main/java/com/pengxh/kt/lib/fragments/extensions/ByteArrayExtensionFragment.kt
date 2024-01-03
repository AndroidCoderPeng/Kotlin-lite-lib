package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentByteArrayExtensionBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.toASCII

class ByteArrayExtensionFragment : KotlinBaseFragment<FragmentByteArrayExtensionBinding>() {

    private val kTag = "ByteArrayExtensionFragment"

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentByteArrayExtensionBinding {
        return FragmentByteArrayExtensionBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.parseButton.setOnClickListener {
            val hex = byteArrayOf(51, 51, 50, 48, 48, 48, 48, 49, 48, 48, 48, 50, 13, 10).toASCII()
            binding.hexStringView.text = hex
        }
    }
}