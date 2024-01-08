package com.pengxh.kt.lib.utils

import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("/jisuapi/get")
    suspend fun getNewsByPage(
        @Query("appkey") appkey: String,
        @Query("channel") channel: String,
        @Query("num") num: Int,
        @Query("start") start: Int
    ): String
}