package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pengxh.kt.lib.databinding.FragmentUtilsHtmlRenderBinding
import com.pengxh.kt.lib.model.NewsListModel
import com.pengxh.kt.lib.utils.LocaleConstant
import com.pengxh.kt.lib.view.BigImageActivity
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.navigatePageTo
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.utils.HtmlRenderEngine
import com.pengxh.kt.lite.utils.HttpRequestKit


class HtmlRenderEngineFragment : KotlinBaseFragment<FragmentUtilsHtmlRenderBinding>() {

    private val gson = Gson()

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsHtmlRenderBinding {
        return FragmentUtilsHtmlRenderBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        HttpRequestKit.Builder()
            .setAuthentication()
            .setRequestTarget(LocaleConstant.CONTENT_API)
            .setOnHttpRequestListener(object : HttpRequestKit.OnHttpRequestListener {
                override fun onSuccess(result: String) {
                    val news = gson.fromJson<NewsListModel>(
                        result, object : TypeToken<NewsListModel>() {}.type
                    )

                    if (news.status == 0) {
                        renderHtmlText(news.result.list.first().content)
                    } else {
                        "请求失败，错误码：$${news.status}".show(requireContext())
                    }
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