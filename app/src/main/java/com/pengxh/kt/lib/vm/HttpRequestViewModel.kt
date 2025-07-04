package com.pengxh.kt.lib.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pengxh.kt.lib.model.NewsListModel
import com.pengxh.kt.lib.utils.RetrofitServiceManager
import com.pengxh.kt.lite.extensions.launch
import com.pengxh.kt.lite.extensions.unpackingResponse
import com.pengxh.kt.lite.utils.HttpResponseState

class HttpRequestViewModel : ViewModel() {

    private val gson by lazy { Gson() }
    val newsListData = MutableLiveData<HttpResponseState<NewsListModel>>()

    fun getNewsByPage(channel: String, start: Int) = launch({
        newsListData.value = HttpResponseState.Loading
        val response = RetrofitServiceManager.getNewsByPage(channel, start)
        val (code, message) = response.getResponseHeader()
        when (code) {
            0 -> newsListData.value = HttpResponseState.Success(
                unpackingResponse<NewsListModel>(response)
            )

            else -> newsListData.value = HttpResponseState.Error(code, message)
        }
    }, {
        newsListData.value = HttpResponseState.Error(500, it.message ?: "Unknown error", it)
    })

    private fun String.getResponseHeader(): Pair<Int, String> {
        if (this.isBlank()) {
            return Pair(404, "Invalid Response")
        }
        val jsonObject = gson.fromJson(this, JsonObject::class.java)
        val code = jsonObject.get("code").asInt
        val message = jsonObject.get("message").asString
        return Pair(code, message)
    }
}