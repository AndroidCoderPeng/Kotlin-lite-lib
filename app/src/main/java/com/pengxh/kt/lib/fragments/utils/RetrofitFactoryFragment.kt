package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.pengxh.kt.lib.databinding.FragmentUtilsRetrofitFactoryBinding
import com.pengxh.kt.lib.utils.RetrofitService
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.utils.RetrofitFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RetrofitFactoryFragment : KotlinBaseFragment<FragmentUtilsRetrofitFactoryBinding>() {

    private val api by lazy {
        val httpConfig = "https://api.jisuapi.com"
        RetrofitFactory.createRetrofit<RetrofitService>(httpConfig)
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsRetrofitFactoryBinding {
        return FragmentUtilsRetrofitFactoryBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        //仅做RetrofitFactory示例，实际业务应该降请求放在ViewModel中
        lifecycleScope.launch(Dispatchers.IO) {
            val response = getNewsByPage("头条", 1)
            withContext(Dispatchers.Main) {
                binding.textView.text = response
            }
        }
    }

    private suspend fun getNewsByPage(channel: String, start: Int): String {
        return api.getNewsByPage(channel, start, 10, "32736cbe845d7a70")
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}