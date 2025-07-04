package com.pengxh.kt.lib.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pengxh.kt.lib.model.NewsListModel
import com.pengxh.kt.lib.utils.RetrofitServiceManager
import com.pengxh.kt.lite.extensions.launch
import com.pengxh.kt.lite.extensions.unpackingResponse
import com.pengxh.kt.lite.utils.HttpResponse

class HttpRequestViewModel : ViewModel() {

    private val gson by lazy { Gson() }
    val newsListData = MutableLiveData<HttpResponse<NewsListModel>>()

    fun getNewsByPage(channel: String, start: Int) = launch({
        newsListData.value = HttpResponse.Loading()
        val response = RetrofitServiceManager.getNewsByPage(channel, start)
        val header = response.getResponseHeader()
        when (header.first) {
            0 -> newsListData.value = HttpResponse.Success(
                unpackingResponse<NewsListModel>(response)
            )

            else -> newsListData.value = HttpResponse.Error(header.second)
        }
    }, {
        newsListData.value = HttpResponse.Error(it.message ?: "Unknown error")
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