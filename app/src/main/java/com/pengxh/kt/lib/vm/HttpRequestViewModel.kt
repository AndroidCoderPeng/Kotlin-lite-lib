package com.pengxh.kt.lib.vm

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pengxh.kt.lib.model.NewsListModel
import com.pengxh.kt.lib.utils.RetrofitServiceManager
import com.pengxh.kt.lite.extensions.launch
import com.pengxh.kt.lite.extensions.unpackingResponse

class HttpRequestViewModel : ViewModel() {

    val gson by lazy { Gson() }

    fun getNewsByPage(
        channel: String,
        start: Int,
        onLoading: () -> Unit,
        onSuccess: (NewsListModel) -> Unit,
        onFailed: (String) -> Unit
    ) = launch({
        onLoading()
        val response = RetrofitServiceManager.getNewsByPage(channel, start)
        val header = response.getResponseHeader()
        when (header.first) {
            0 -> onSuccess(unpackingResponse<NewsListModel>(response))
            else -> onFailed(header.second)
        }
    }, {
        onFailed(it.message ?: "Unknown error")
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