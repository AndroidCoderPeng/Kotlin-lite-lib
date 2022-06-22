package com.pengxh.kt.lite.extensions

import android.util.Log
import com.pengxh.kt.lite.callback.OnHttpRequestListener
import com.pengxh.kt.lite.utils.Constant
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeUnit

fun Request.doRequest(listener: OnHttpRequestListener) {
    Observable.create(Observable.OnSubscribe<Response?> { subscriber ->
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("Request", "log ===> $message")
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
            val response = client.newCall(this).execute()
            subscriber.onNext(response)
        } catch (e: IOException) {
            subscriber.onError(e)
        }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Observer<Response?> {

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                listener.onFailure(e)
            }

            override fun onNext(response: Response?) {
                listener.onSuccess(response)
            }
        })
}