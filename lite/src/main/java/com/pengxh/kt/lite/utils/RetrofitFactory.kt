package com.pengxh.kt.lite.utils

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.annotations.NotNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {
    private const val kTag = "RetrofitFactory"
    val httpClient: OkHttpClient by lazy { createHttpClient() }

    inline fun <reified T> createRetrofit(httpConfig: String): T {
        return Retrofit.Builder()
            .baseUrl(httpConfig)
            .addConverterFactory(ScalarsConverterFactory.create())          //字符串转换器
            .addConverterFactory(GsonConverterFactory.create())             //Gson转换器
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())    //协程请求适配器
            .client(httpClient) //log拦截器
            .build().create(T::class.java)
    }

    private fun createHttpClient(): OkHttpClient { //日志显示级别
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(@NotNull message: String) {
                Log.d(kTag, ">>>>> $message")
            }
        })
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
            .connectTimeout(Constant.HTTP_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constant.HTTP_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constant.HTTP_TIMEOUT, TimeUnit.SECONDS)
        return builder.addInterceptor(interceptor).build()
    }
}