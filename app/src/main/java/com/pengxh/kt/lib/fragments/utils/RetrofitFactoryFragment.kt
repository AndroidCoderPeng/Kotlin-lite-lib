package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.pengxh.kt.lib.databinding.FragmentUtilsRetrofitFactoryBinding
import com.pengxh.kt.lib.vm.HttpRequestViewModel
import com.pengxh.kt.lite.base.KotlinBaseFragment

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
        httpRequestViewModel.getNewsByPage(requireContext(),"头条", 1)
        httpRequestViewModel.httpRequestResult.observe(this) { response ->
            binding.textView.text = response
        }
    }


    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}