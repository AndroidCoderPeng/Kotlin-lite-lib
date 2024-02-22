package com.pengxh.kt.lite.utils

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {
    const val kTag = "RetrofitFactory"

    /**
     * @param httpConfig 接口地址
     * @param timeout 网络请求超时时间，单位：秒
     * */
    inline fun <reified T> createRetrofit(
        httpConfig: String, timeout: Long = Constant.HTTP_TIMEOUT
    ): T {
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d(kTag, ">>>>> $message")
            }
        })
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
        return Retrofit.Builder()
            .baseUrl(httpConfig)
            .addConverterFactory(ScalarsConverterFactory.create())          //字符串转换器
            .addConverterFactory(GsonConverterFactory.create())             //Gson转换器
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())    //协程请求适配器
            .client(httpClientBuilder.build()) //log拦截器
            .build().create(T::class.java)
    }
}