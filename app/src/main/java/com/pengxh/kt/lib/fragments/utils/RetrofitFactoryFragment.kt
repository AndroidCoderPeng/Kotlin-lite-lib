package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.pengxh.kt.lib.databinding.FragmentUtilsRetrofitFactoryBinding
import com.pengxh.kt.lib.vm.HttpRequestViewModel
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.utils.LoadState
import com.pengxh.kt.lite.utils.LoadingDialogHub

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
        httpRequestViewModel.getNewsByPage(requireContext(), "头条", 1)
        httpRequestViewModel.httpRequestResult.observe(this) { response ->
            binding.textView.text = response
        }
    }


    override fun observeRequestState() {
        httpRequestViewModel.loadState.observe(this) {
            when (it) {
                LoadState.Loading -> {
                    LoadingDialogHub.show(requireActivity(), "数据请求中，请稍后...")
                }

                LoadState.Success -> {
                    LoadingDialogHub.dismiss()
                }

                else -> {
                    LoadingDialogHub.dismiss()
                }
            }
        }
    }

    override fun initEvent() {

    }
}