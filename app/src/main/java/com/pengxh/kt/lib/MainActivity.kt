package com.pengxh.kt.lib

import android.graphics.Color
import android.os.Handler
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.extensions.convertColor
import com.pengxh.kt.lite.extensions.readAssetsFile
import com.pengxh.kt.lite.extensions.setAnimation
import com.pengxh.kt.lite.utils.WeakReferenceHandler
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


class MainActivity : KotlinBaseActivity() {

    companion object {
        lateinit var timer: Timer
    }

    private val kTag = "MainActivity"
    private lateinit var weakReferenceHandler: WeakReferenceHandler
    private var noticeBeans: MutableList<NoticeListModel.DataModel.RowsModel> = ArrayList()
    private var currentIndex = 0

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun setupTopBarLayout() {

    }

    override fun observeRequestState() {

    }

    override fun initData() {
        val response = readAssetsFile("Test.json")
        val it = Gson().fromJson<NoticeListModel>(
            response, object : TypeToken<NoticeListModel>() {}.type
        )
        noticeBeans = it.data.rows

        noticeSwitcherView.setFactory {
            val textView = TextView(this)
            textView.setTextColor(Color.BLACK)
            textView
        }
        noticeSwitcherView.setAnimation()

        timeSwitcherView.setFactory {
            val textView = TextView(this)
            textView.setTextColor(R.color.hintColor.convertColor(this))
            textView
        }
        timeSwitcherView.setAnimation()

        weakReferenceHandler = WeakReferenceHandler(callback)
        //消息滚动Timer
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                weakReferenceHandler.sendEmptyMessage(2023030601)
            }
        }, 0, 3000)
    }

    private val callback = Handler.Callback { msg ->
        when (msg.what) {
            2023030601 -> {
                if (noticeBeans.size != 0) {
                    val model = noticeBeans[currentIndex % noticeBeans.size]

                    noticeSwitcherView.setText(model.noticeTitle)

                    val deltaT = model.createTime.diffCurrentTime()
                    val diffTime = if (deltaT < 24) {
                        "${deltaT}小时前"
                    } else {
                        model.createTime.formatToDate()
                    }
                    timeSwitcherView.setText(diffTime)
                    currentIndex++
                } else {
                    noticeSwitcherView.setText("暂无新消息")
                    timeSwitcherView.setText("--:--")
                }
            }
        }
        true
    }

    override fun initEvent() {

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    /**
     * 时间差-小时
     * */
    private fun String.diffCurrentTime(): Int {
        if (this.isBlank()) {
            return 0
        }
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val date = simpleDateFormat.parse(this)
        val diff = abs(System.currentTimeMillis() - date.time)
        return (diff / (3600000)).toInt()
    }

    private fun String.formatToDate(): String {
        if (this.isBlank()) {
            return this
        }
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val date = simpleDateFormat.parse(this)

        val dateFormat = SimpleDateFormat("MM-dd", Locale.CHINA)
        return dateFormat.format(date)
    }
}