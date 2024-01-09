package com.pengxh.kt.lib.fragments.utils

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentUtilsHttpRequestBinding
import com.pengxh.kt.lib.model.NewsListModel
import com.pengxh.kt.lib.utils.LocalConstant
import com.pengxh.kt.lite.adapter.NormalRecyclerAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.divider.RecyclerViewItemDivider
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.utils.HttpRequestHub

class HttpRequestFragment : KotlinBaseFragment<FragmentUtilsHttpRequestBinding>() {

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
        HttpRequestHub.Builder()
            .setRequestTarget(LocalConstant.TARGET_API)
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
                    val listAdapter = object :
                        NormalRecyclerAdapter<NewsListModel.ResultModel.ListModel>(
                            R.layout.item_http_request_rv_l, listModel.result.list
                        ) {
                        override fun convertView(
                            viewHolder: ViewHolder, position: Int,
                            item: NewsListModel.ResultModel.ListModel
                        ) {
                            viewHolder.setText(R.id.textView, item.title)
                        }
                    }
                    binding.recyclerView.addItemDecoration(RecyclerViewItemDivider(1, Color.LTGRAY))
                    binding.recyclerView.adapter = listAdapter
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