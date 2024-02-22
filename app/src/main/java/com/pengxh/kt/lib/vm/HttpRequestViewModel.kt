package com.pengxh.kt.lib.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.pengxh.kt.lib.utils.RetrofitServiceManager
import com.pengxh.kt.lite.base.BaseViewModel
import com.pengxh.kt.lite.extensions.launch
import com.pengxh.kt.lite.extensions.show

class HttpRequestViewModel : BaseViewModel() {

    private val kTag = "HttpRequestViewModel"
    val httpRequestResult = MutableLiveData<String>()

    fun getNewsByPage(context: Context, channel: String, start: Int) = launch({
        val response = RetrofitServiceManager.getNewsByPage(channel, start)
        httpRequestResult.value = response
    }, {
        it.localizedMessage?.show(context)
    })
}