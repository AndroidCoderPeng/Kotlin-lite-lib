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

class HttpRequestKit(builder: Builder) : LifecycleOwner {

    private val kTag = "HttpRequestKit"
    private val registry = LifecycleRegistry(this)

    class Builder {
        lateinit var key: String
        lateinit var value: String
        lateinit var url: String
        lateinit var httpRequestListener: OnHttpRequestListener

        /**
         * 设置网络请求鉴权
         */
        fun setAuthentication(key: String = "token", value: String = ""): Builder {
            this.key = key
            this.value = value
            return this
        }

        /**
         * 设置网络请求接口地址
         * */
        fun setRequestTarget(url: String): Builder {
            this.url = url
            return this
        }

        /**
         * 设置网络请求回调监听
         * */
        fun setOnHttpRequestListener(httpRequestListener: OnHttpRequestListener): Builder {
            this.httpRequestListener = httpRequestListener
            return this
        }

        fun build(): HttpRequestKit {
            return HttpRequestKit(this)
        }
    }

    private val key = builder.key
    private val value = builder.value
    private val url = builder.url
    private val listener = builder.httpRequestListener

    /**
     * 发起网络请求
     * */
    fun start() {
        if (url.isBlank()) {
            listener.onFailure(IllegalArgumentException("url is empty"))
            return
        }
        //构建Request
        val request = Request.Builder().addHeader(key, value).url(url).get().build()
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
                        listener.onSuccess(string())
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    listener.onFailure(e)
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