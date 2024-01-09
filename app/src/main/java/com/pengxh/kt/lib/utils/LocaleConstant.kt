package com.pengxh.kt.lib.utils

object LocaleConstant {
    const val SERVICE_UUID = "0003cdd0-0000-1000-8000-00805f9b0131" //连接设备的UUID
    const val WRITE_CHARACTERISTIC_UUID = "0003cdd2-0000-1000-8000-00805f9b0131" //写数据特征值UUID
    const val READ_CHARACTERISTIC_UUID = "0003cdd1-0000-1000-8000-00805f9b0131" //读数据特征值UUID

    val CITIES = listOf(
        "安徽",
        "北京",
        "滨海",
        "重庆",
        "大连",
        "恩施",
        "福建",
        "甘肃",
        "广东",
        "广西",
        "贵州",
        "海南",
        "河北",
        "河南",
        "黑龙江",
        "湖北",
        "湖南",
        "黄石",
        "吉林",
        "江苏",
        "江西",
        "锦州",
        "荆门",
        "九江",
        "辽宁",
        "洛阳",
        "内蒙古",
        "宁波",
        "宁夏",
        "青岛",
        "青海",
        "三亚",
        "山东",
        "山西",
        "陕西",
        "上海",
        "深圳",
        "十堰",
        "四川",
        "天津",
        "西藏",
        "厦门",
        "襄阳",
        "孝感",
        "新疆",
        "新乡",
        "忻州",
        "宜昌",
        "云南",
        "湛江",
        "浙江",
        "珠海"
    )

    //API测试接口地址
    const val TARGET_API =
        "https://api.jisuapi.com/news/get?channel=头条&start=0&num=15&appkey=32736cbe845d7a70"
}