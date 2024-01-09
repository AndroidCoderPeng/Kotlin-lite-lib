package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.pengxh.kt.lib.databinding.FragmentUtilsHtmlRenderBinding
import com.pengxh.kt.lib.model.NewsListModel
import com.pengxh.kt.lib.utils.LocaleConstant
import com.pengxh.kt.lib.view.BigImageActivity
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.navigatePageTo
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.utils.HtmlRenderEngine
import com.pengxh.kt.lite.utils.HttpRequestHub


class HtmlRenderEngineFragment : KotlinBaseFragment<FragmentUtilsHtmlRenderBinding>() {

    private val gson by lazy { Gson() }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsHtmlRenderBinding {
        return FragmentUtilsHtmlRenderBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        HttpRequestHub.Builder()
            .setRequestTarget(LocaleConstant.TARGET_API)
            .setOnHttpRequestListener(object : HttpRequestHub.OnHttpRequestListener {
                override fun onSuccess(result: String) {
                    val element = JsonParser.parseString(result)
                    val jsonObject = element.asJsonObject
                    if (jsonObject.get("status").asInt != 0) {
                        jsonObject.get("msg").asString.show(requireContext())
                        return
                    }

                    val listModel = gson.fromJson<NewsListModel>(
                        result, object : TypeToken<NewsListModel>() {}.type
                    )
                    renderHtmlText(listModel.result.list[2].content)
                }

                override fun onFailure(throwable: Throwable) {

                }
            }).build().start()
    }

    private fun renderHtmlText(content: String) {
        HtmlRenderEngine.Builder()
            .setContext(requireContext())
            .setHtmlContent(content)
            .setTargetView(binding.htmlTextView)
            .setOnGetImageSourceListener(object : HtmlRenderEngine.OnGetImageSourceListener {
                override fun imageSource(url: String) {
                    val urls = ArrayList<String>()
                    urls.add(url)
                    requireContext().navigatePageTo<BigImageActivity>(0, urls)
                }
            }).build().load()
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}