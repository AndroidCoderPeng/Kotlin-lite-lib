package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.pengxh.kt.lib.databinding.FragmentExtensionViewModelBinding
import com.pengxh.kt.lib.example.ViewModelExample
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.vm.LoadState

class ViewModelExtensionFragment : KotlinBaseFragment<FragmentExtensionViewModelBinding>() {

    private val kTag = "ViewModelExtensionFragment"
    private lateinit var viewModelExample: ViewModelExample

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionViewModelBinding {
        return FragmentExtensionViewModelBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        viewModelExample = ViewModelProvider(this)[ViewModelExample::class.java]
        viewModelExample.getTestDataByPage("", 1)
        viewModelExample.resultModel.observe(this) {
            if (it.code == 200) {
                //...
            }
        }
    }

    override fun observeRequestState() {
        viewModelExample.loadState.observe(this) {
            when (it) {
                LoadState.Loading -> Log.d(kTag, "加载数据中，请稍后...")
                LoadState.Success -> Log.d(kTag, "加载成功")
                LoadState.Fail -> Log.d(kTag, "加载失败，请重试")
            }
        }
    }

    override fun initEvent() {

    }
}