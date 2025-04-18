package com.pengxh.kt.lib.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.pengxh.kt.lib.model.NewsListModel
import com.pengxh.kt.lib.utils.RetrofitServiceManager
import com.pengxh.kt.lite.base.BaseViewModel
import com.pengxh.kt.lite.extensions.launch
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.utils.LoadState

class HttpRequestViewModel : BaseViewModel() {

    val gson by lazy { Gson() }
    val httpRequestResult = MutableLiveData<NewsListModel>()

    fun getNewsByPage(context: Context, channel: String, start: Int) = launch({
        loadState.value = LoadState.Loading
        val response = RetrofitServiceManager.getNewsByPage(channel, start)
        val header = response.getResponseHeader()
        when (header.first) {
            0 -> {
                httpRequestResult.value = gson.fromJson<NewsListModel>(
                    response, object : TypeToken<NewsListModel>() {}.type
                )
                loadState.value = LoadState.Success
            }

            else -> {
                loadState.value = LoadState.Fail
                header.second.show(context)
            }
        }
    }, {
        loadState.value = LoadState.Fail
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