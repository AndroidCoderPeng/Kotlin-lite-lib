package com.pengxh.kt.lib.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.pengxh.kt.lib.utils.RetrofitServiceManager
import com.pengxh.kt.lite.base.BaseViewModel
import com.pengxh.kt.lite.extensions.launch
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.utils.LoadState

class HttpRequestViewModel : BaseViewModel() {

    private val kTag = "HttpRequestViewModel"
    val httpRequestResult = MutableLiveData<String>()

    fun getNewsByPage(context: Context, channel: String, start: Int) = launch({
        loadState.value = LoadState.Loading
        val response = RetrofitServiceManager.getNewsByPage(channel, start)
        httpRequestResult.value = response
        loadState.value = LoadState.Success
    }, {
        it.localizedMessage?.show(context)
        loadState.value = LoadState.Fail
    })
}