package com.pengxh.kt.lib.utils

import com.pengxh.kt.lite.utils.RetrofitFactory.createRetrofit


object RetrofitServiceManager {

    private val api by lazy {
        val httpConfig = "https://api.jisuapi.com"
        createRetrofit<RetrofitService>(httpConfig)
    }

    suspend fun getNewsByPage(channel: String, start: Int): String {
        return api.getNewsByPage(channel, start, 10, "32736cbe845d7a70")
    }
}