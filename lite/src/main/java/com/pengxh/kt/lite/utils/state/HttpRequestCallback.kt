package com.pengxh.kt.lite.utils.state

interface HttpRequestCallback {
    fun onStateChanged(state: HttpRequestState, message: String = "")
}