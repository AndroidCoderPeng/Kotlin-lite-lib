package com.pengxh.kt.lib.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentBaseSingletonBinding
import com.pengxh.kt.lib.example.SingletonExample
import com.pengxh.kt.lite.base.KotlinBaseFragment

class BaseSingletonFragment : KotlinBaseFragment<FragmentBaseSingletonBinding>() {

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBaseSingletonBinding {
        return FragmentBaseSingletonBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val singletonExample = SingletonExample(requireContext())
        singletonExample.addAction(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

            }
        }, "自己的Action")
        singletonExample.destroy("自己的Action")
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}