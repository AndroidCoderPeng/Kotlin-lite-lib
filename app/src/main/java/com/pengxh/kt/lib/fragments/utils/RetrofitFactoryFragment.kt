package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.pengxh.kt.lib.databinding.FragmentUtilsRetrofitFactoryBinding
import com.pengxh.kt.lib.vm.HttpRequestViewModel
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.utils.LoadState
import com.pengxh.kt.lite.utils.LoadingDialog

class RetrofitFactoryFragment : KotlinBaseFragment<FragmentUtilsRetrofitFactoryBinding>() {

    private lateinit var httpRequestViewModel: HttpRequestViewModel

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsRetrofitFactoryBinding {
        return FragmentUtilsRetrofitFactoryBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        httpRequestViewModel = ViewModelProvider(this)[HttpRequestViewModel::class.java]
        httpRequestViewModel.getNewsByPage("头条", 1)
        httpRequestViewModel.httpRequestResult.observe(this) {
            if (it.status == 0) {
                binding.textView.text = it.result.list.first().title
            }
        }
    }


    override fun observeRequestState() {
        httpRequestViewModel.loadState.observe(this) {
            when (it) {
                LoadState.Loading -> {
                    LoadingDialog.show(requireActivity(), "数据请求中，请稍后...")
                }

                LoadState.Success -> {
                    LoadingDialog.dismiss()
                }

                else -> {
                    LoadingDialog.dismiss()
                }
            }
        }
    }

    override fun initEvent() {

    }
}