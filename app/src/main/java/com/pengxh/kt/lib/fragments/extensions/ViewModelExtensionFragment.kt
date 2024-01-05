package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.pengxh.kt.lib.databinding.FragmentViewModelExtensionBinding
import com.pengxh.kt.lib.example.ViewModelExample
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.utils.LoadingDialogHub
import com.pengxh.kt.lite.vm.LoadState

class ViewModelExtensionFragment : KotlinBaseFragment<FragmentViewModelExtensionBinding>() {

    private lateinit var viewModelExample: ViewModelExample

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentViewModelExtensionBinding {
        return FragmentViewModelExtensionBinding.inflate(inflater, container, false)
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
                LoadState.Loading -> {
                    LoadingDialogHub.show(requireActivity(), "加载数据中，请稍后...")
                }

                else -> LoadingDialogHub.dismiss()
            }
        }
    }

    override fun initEvent() {

    }
}