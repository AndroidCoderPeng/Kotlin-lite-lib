package com.pengxh.kt.lite.utils

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

class HttpRequestHub : LifecycleOwner {
    private val kTag = "HttpRequestHub"
    private val registry = LifecycleRegistry(this)
    private lateinit var url: String
    private lateinit var httpRequestListener: OnHttpRequestListener

    /**
     * 设置网络请求接口地址
     * */
    fun setRequestTarget(url: String): HttpRequestHub {
        this.url = url
        return this
    }

    /**
     * 设置网络请求回调监听
     * */
    fun setOnHttpRequestListener(httpRequestListener: OnHttpRequestListener): HttpRequestHub {
        this.httpRequestListener = httpRequestListener
        return this
    }

    /**
     * 发起网络请求
     * */
    fun start() {
        if (url.isBlank()) {
            httpRequestListener.onFailure(IllegalArgumentException("url is empty"))
            return
        }
        //构建Request
        val request = Request.Builder().url(url).get().build()
        lifecycleScope.launch(Dispatchers.IO) {
            val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d(kTag, ">>>>> $message")
                }
            })
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(Constant.HTTP_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(Constant.HTTP_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.HTTP_TIMEOUT, TimeUnit.SECONDS)
                .build()
            try {
                val response = client.newCall(request).execute()
                response.body?.apply {
                    withContext(Dispatchers.Main) {
                        httpRequestListener.onSuccess(string())
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    httpRequestListener.onFailure(e)
                }
            }
        }
    }

    interface OnHttpRequestListener {
        fun onSuccess(result: String)

        fun onFailure(throwable: Throwable)
    }

    override fun getLifecycle(): Lifecycle {
        return registry
    }
}