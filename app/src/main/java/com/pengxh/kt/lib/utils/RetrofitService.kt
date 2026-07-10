package com.pengxh.kt.lib.utils

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    // Retrofit 直接返回 String可能会因为服务端编码设置出现乱码，更稳妥的写法是返回 ResponseBody
    @GET("/news/get")
    suspend fun getNewsByPage(
        @Query("channel") channel: String,
        @Query("start") start: Int,
        @Query("num") num: Int,
        @Query("appkey") appkey: String,
    ): ResponseBody
}