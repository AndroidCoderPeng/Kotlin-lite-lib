package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.pengxh.kt.lib.databinding.FragmentUtilsRetrofitFactoryBinding
import com.pengxh.kt.lib.vm.HttpRequestViewModel
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.utils.HttpResponseState
import com.pengxh.kt.lite.utils.LoadingDialog

class RetrofitFactoryFragment : KotlinBaseFragment<FragmentUtilsRetrofitFactoryBinding>() {

    private val httpRequestViewModel by lazy { ViewModelProvider(this)[HttpRequestViewModel::class.java] }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsRetrofitFactoryBinding {
        return FragmentUtilsRetrofitFactoryBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        httpRequestViewModel.getNewsByPage("头条", 1)
    }


    override fun observeRequestState() {
        httpRequestViewModel.newsListData.observe(this) { response ->
            when (response) {
                is HttpResponseState.Loading -> {
                    LoadingDialog.show(requireActivity(), "数据请求中，请稍后...")
                }

                is HttpResponseState.Success -> {
                    LoadingDialog.dismiss()
                    binding.textView.text = response.body.result?.list?.first()?.content
                }

                is HttpResponseState.Error -> {
                    LoadingDialog.dismiss()
                    response.message.show(requireContext())
                }

                else -> {}
            }
        }
    }

    override fun initEvent() {

    }
}