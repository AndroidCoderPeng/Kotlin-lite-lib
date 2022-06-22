package com.pengxh.kt.lite.callback

import okhttp3.Response

interface OnHttpRequestListener {
    fun onSuccess(response: Response?)

    fun onFailure(throwable: Throwable?)
}