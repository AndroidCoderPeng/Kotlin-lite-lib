package com.pengxh.kt.lib.utils

object LocalConstant {
    const val SERVICE_UUID = "0003cdd0-0000-1000-8000-00805f9b0131" //连接设备的UUID
    const val WRITE_CHARACTERISTIC_UUID = "0003cdd2-0000-1000-8000-00805f9b0131" //写数据特征值UUID
    const val READ_CHARACTERISTIC_UUID = "0003cdd1-0000-1000-8000-00805f9b0131" //读数据特征值UUID

    //API测试接口地址
    const val TARGET_API =
        "https://api.jisuapi.com/news/get?channel=头条&start=0&num=15&appkey=e957ed7ad90436a57e604127d9d8fa32"
}