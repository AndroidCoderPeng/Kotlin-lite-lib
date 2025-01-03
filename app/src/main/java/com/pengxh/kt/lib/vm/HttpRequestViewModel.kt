package com.pengxh.kt.lib.vm

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.pengxh.kt.lib.model.NewsListModel
import com.pengxh.kt.lib.utils.RetrofitServiceManager
import com.pengxh.kt.lite.base.BaseViewModel
import com.pengxh.kt.lite.extensions.launch
import com.pengxh.kt.lite.utils.LoadState

class HttpRequestViewModel : BaseViewModel() {

    private val kTag = "HttpRequestViewModel"
    private val gson = Gson()
    val httpRequestResult = MutableLiveData<NewsListModel>()

    fun getNewsByPage(channel: String, start: Int) = launch({
        loadState.value = LoadState.Loading
        val response = RetrofitServiceManager.getNewsByPage(channel, start)
        when (response.getResponseCode()) {
            0 -> {
                httpRequestResult.value = gson.fromJson<NewsListModel>(
                    response, object : TypeToken<NewsListModel>() {}.type
                )
                loadState.value = LoadState.Success
            }

            else -> {
                loadState.value = LoadState.Fail
            }
        }
    }, {
        loadState.value = LoadState.Fail
    })

    private fun String.getResponseCode(): Int {
        if (this.isBlank()) {
            return 404
        }
        val element = JsonParser.parseString(this)
        val jsonObject = element.asJsonObject
        return jsonObject.get("status").asInt
    }
}