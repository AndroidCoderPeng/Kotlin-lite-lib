package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pengxh.kt.lib.databinding.FragmentUtilsHtmlRenderBinding
import com.pengxh.kt.lib.model.NewsListModel
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
            .setRequestTarget("https://way.jd.com/jisuapi/get?channel=头条&num=10&start=1&appkey=e957ed7ad90436a57e604127d9d8fa32")
            .setOnHttpRequestListener(object : HttpRequestHub.OnHttpRequestListener {
                override fun onSuccess(result: String) {
                    if (result.contains("请求超过次数限制")) {
                        "请求超过次数限制".show(requireContext())
                        return
                    }
                    val listModel = gson.fromJson<NewsListModel>(
                        result, object : TypeToken<NewsListModel>() {}.type
                    )
                    renderHtmlText(listModel.result.result.list[0].content)
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