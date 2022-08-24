package com.pengxh.kt.lite.extensions

import android.content.Context
import com.amap.api.maps.model.Poi
import com.amap.api.navi.AmapNaviPage
import com.amap.api.navi.AmapNaviParams
import com.amap.api.navi.AmapNaviType
import com.amap.api.navi.AmapPageType

/**
 * 导航扩展函数
 * */
fun Poi.showRouteOnMap(context: Context) {
    val params = AmapNaviParams(
        null, null, this,
        AmapNaviType.WALK,
        AmapPageType.ROUTE
    )
    AmapNaviPage.getInstance().showRouteActivity(context, params, null)
}

fun Poi.showBusRouteOnMap(context: Context) {
    val params = AmapNaviParams(
        null, null, this,
        AmapNaviType.DRIVER,
        AmapPageType.ROUTE
    )
    AmapNaviPage.getInstance().showRouteActivity(context, params, null)
}