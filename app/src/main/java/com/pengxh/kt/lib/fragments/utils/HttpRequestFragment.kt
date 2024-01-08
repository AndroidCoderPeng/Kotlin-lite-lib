package com.pengxh.kt.lib.fragments.utils

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentUtilsHttpRequestBinding
import com.pengxh.kt.lib.model.NewsListModel
import com.pengxh.kt.lite.adapter.NormalRecyclerAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.divider.RecyclerViewItemDivider
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.utils.HttpRequestHub

class HttpRequestFragment : KotlinBaseFragment<FragmentUtilsHttpRequestBinding>() {

    private val httpRequestHub by lazy { HttpRequestHub() }
    private val gson by lazy { Gson() }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsHttpRequestBinding {
        return FragmentUtilsHttpRequestBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        httpRequestHub.setRequestTarget("https://way.jd.com/jisuapi/get?channel=头条&num=15&start=1&appkey=e957ed7ad90436a57e604127d9d8fa32")
            .setOnHttpRequestListener(object : HttpRequestHub.OnHttpRequestListener {
                override fun onSuccess(result: String) {
                    if (result.contains("请求超过次数限制")) {
                        "请求超过次数限制".show(requireContext())
                        return
                    }
                    val listModel = gson.fromJson<NewsListModel>(
                        result, object : TypeToken<NewsListModel>() {}.type
                    )
                    val listAdapter = object :
                        NormalRecyclerAdapter<NewsListModel.ResultModel.ResultSubModel.ListModel>(
                            R.layout.item_http_request_rv_l, listModel.result.result.list
                        ) {
                        override fun convertView(
                            viewHolder: ViewHolder, position: Int,
                            item: NewsListModel.ResultModel.ResultSubModel.ListModel
                        ) {
                            viewHolder.setText(R.id.textView, item.title)
                        }
                    }
                    binding.recyclerView.addItemDecoration(RecyclerViewItemDivider(1, Color.LTGRAY))
                    binding.recyclerView.adapter = listAdapter
                }

                override fun onFailure(throwable: Throwable) {

                }
            }).start()
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}