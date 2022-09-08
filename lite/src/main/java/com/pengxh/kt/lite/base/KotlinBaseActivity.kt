package com.pengxh.kt.lite.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.pengxh.kt.lite.extensions.isNetworkConnected
import com.pengxh.kt.lite.utils.BroadcastManager
import com.pengxh.kt.lite.utils.PageNavigationManager
import com.pengxh.kt.lite.widget.dialog.NoNetworkDialog

abstract class KotlinBaseActivity : AppCompatActivity() {

    private lateinit var broadcastManager: BroadcastManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initLayoutView())
        setupTopBarLayout()
        initData()
        observeRequestState()
        initEvent()
        PageNavigationManager.addActivity(this)
        broadcastManager = BroadcastManager.obtainInstance(this)
        broadcastManager.addAction(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (!context!!.isNetworkConnected()) {
                        /**
                         * 捕获用户在有网情况下登录进入APP，后来又处于断网状态会导致闪退的Bug
                         * */
                        try {
                            NoNetworkDialog.Builder()
                                .setContext(this@KotlinBaseActivity)
                                .setOnDialogButtonClickListener(object :
                                    NoNetworkDialog.OnDialogButtonClickListener {
                                    override fun onButtonClick() {
                                        startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
                                    }
                                }).build().show()
                        } catch (e: WindowManager.BadTokenException) {
                            e.printStackTrace()
                        }
                    }
                }
            }, ConnectivityManager.CONNECTIVITY_ACTION
        )
    }

    /**
     * 初始化xml布局
     */
    abstract fun initLayoutView(): Int

    /**
     * 特定页面定制沉浸式状态栏
     */
    abstract fun setupTopBarLayout()

    /**
     * 初始化默认数据
     */
    abstract fun initData()

    /**
     * 数据请求状态监听
     */
    abstract fun observeRequestState()

    /**
     * 初始化业务逻辑
     */
    abstract fun initEvent()

    /**
     * 取消协程
     * */
    override fun onDestroy() {
        broadcastManager.destroy(ConnectivityManager.CONNECTIVITY_ACTION)
        super.onDestroy()
    }
}