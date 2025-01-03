package com.pengxh.kt.lite.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

class HttpRequestKit(builder: Builder) {

    private val kTag = "HttpRequestKit"

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
            if (!::key.isInitialized || !::value.isInitialized || !::url.isInitialized || !::httpRequestListener.isInitialized) {
                throw IllegalStateException("All properties must be initialized before building.")
            }
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
        val job = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.Main + job)

        //构建Request
        val request = Request.Builder().addHeader(key, value).url(url).get().build()
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d(kTag, ">>>>> $message")
            }
        })
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(LiteKitConstant.HTTP_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(LiteKitConstant.HTTP_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(LiteKitConstant.HTTP_TIMEOUT, TimeUnit.SECONDS)
            .build()
        scope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    client.newCall(request).execute()
                }
                response.body?.apply {
                    listener.onSuccess(string())
                }
            } catch (e: IOException) {
                listener.onFailure(e)
            }
            job.cancel()
        }
    }

    interface OnHttpRequestListener {
        fun onSuccess(result: String)

        fun onFailure(throwable: Throwable)
    }
}