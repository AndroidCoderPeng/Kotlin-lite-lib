package com.pengxh.kt.lite.callback

import okhttp3.Response

interface IHttpRequestListener {
    fun onSuccess(response: Response?)

    fun onFailure(throwable: Throwable?)
}