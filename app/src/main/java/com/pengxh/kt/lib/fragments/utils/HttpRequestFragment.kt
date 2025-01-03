package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.JsonParser
import com.pengxh.kt.lib.databinding.FragmentUtilsHttpRequestBinding
import com.pengxh.kt.lib.utils.LocaleConstant
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.utils.HttpRequestKit

class HttpRequestFragment : KotlinBaseFragment<FragmentUtilsHttpRequestBinding>() {

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsHttpRequestBinding {
        return FragmentUtilsHttpRequestBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        HttpRequestKit.Builder()
            .setAuthentication()
            .setRequestTarget(LocaleConstant.TARGET_API)
            .setOnHttpRequestListener(object : HttpRequestKit.OnHttpRequestListener {
                override fun onSuccess(result: String) {
                    val element = JsonParser.parseString(result)
                    val jsonObject = element.asJsonObject
                    if (jsonObject.get("code").asInt != 200) {
                        "请求失败".show(requireContext())
                        return
                    }

                    binding.textView.text = jsonObject.get("content").asString
                }

                override fun onFailure(throwable: Throwable) {

                }
            }).build().start()
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}