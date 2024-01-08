package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentUtilsWeakReferenceHandlerBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.utils.WeakReferenceHandler

class WeakReferenceHandlerFragment :
    KotlinBaseFragment<FragmentUtilsWeakReferenceHandlerBinding>(), Handler.Callback {

    private val weakReferenceHandler by lazy { WeakReferenceHandler(this) }

    override fun handleMessage(msg: Message): Boolean {
        if (msg.what == 0) {
            binding.textView.text = "见代码\nWeakReferenceHandlerFragment.kt"
        }
        return true
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsWeakReferenceHandlerBinding {
        return FragmentUtilsWeakReferenceHandlerBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        weakReferenceHandler.sendEmptyMessageDelayed(0, 3000)
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}